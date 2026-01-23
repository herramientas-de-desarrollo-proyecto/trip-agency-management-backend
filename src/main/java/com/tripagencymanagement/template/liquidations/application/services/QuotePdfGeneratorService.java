package com.tripagencymanagement.template.liquidations.application.services;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tripagencymanagement.template.liquidations.domain.entities.DAdditionalServices;
import com.tripagencymanagement.template.liquidations.domain.entities.DFlightBooking;
import com.tripagencymanagement.template.liquidations.domain.entities.DFlightService;
import com.tripagencymanagement.template.liquidations.domain.entities.DHotelBooking;
import com.tripagencymanagement.template.liquidations.domain.entities.DHotelService;
import com.tripagencymanagement.template.liquidations.domain.entities.DTour;
import com.tripagencymanagement.template.liquidations.domain.entities.DTourService;
import com.tripagencymanagement.template.liquidations.presentation.dto.LiquidationWithDetailsDto;

@Service
public class QuotePdfGeneratorService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Blue-600
    private static final Color HEADER_BG = new Color(243, 244, 246); // Gray-100
    private static final Color BORDER_COLOR = new Color(229, 231, 235); // Gray-200

    private final Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, PRIMARY_COLOR);
    private final Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.DARK_GRAY);
    private final Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
    private final Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private final Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);

    public byte[] generateQuotePdf(LiquidationWithDetailsDto liquidation) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 50, 50);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Header
            addHeader(document, liquidation);

            // Customer Info
            addCustomerInfo(document, liquidation);

            // Services
            addFlightServices(document, liquidation.getFlightServices());
            addHotelServices(document, liquidation.getHotelServices());
            addTourServices(document, liquidation.getTourServices());
            addAdditionalServices(document, liquidation.getAdditionalServices());

            // Totals
            addTotals(document, liquidation);

            // Footer
            addFooter(document, liquidation);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return baos.toByteArray();
    }

    private void addHeader(Document document, LiquidationWithDetailsDto liquidation) {
        try {
            // Company Name / Title
            Paragraph title = new Paragraph("PTC TRAVEL AGENCY", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("COTIZACIÓN DE SERVICIOS", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(5);
            document.add(subtitle);

            // Quote Number and Date
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(15);
            headerTable.setSpacingAfter(15);

            PdfPCell quoteCell = new PdfPCell(new Phrase("Cotización N°: " + String.format("%06d", liquidation.getId()), boldFont));
            quoteCell.setBorder(0);
            quoteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(quoteCell);

            String dateStr = liquidation.getCreatedAt() != null 
                ? liquidation.getCreatedAt().format(DATETIME_FORMATTER) 
                : "N/A";
            PdfPCell dateCell = new PdfPCell(new Phrase("Fecha: " + dateStr, normalFont));
            dateCell.setBorder(0);
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(dateCell);

            document.add(headerTable);

            // Separator line
            PdfPTable separatorTable = new PdfPTable(1);
            separatorTable.setWidthPercentage(100);
            PdfPCell separatorCell = new PdfPCell();
            separatorCell.setBorderWidth(0);
            separatorCell.setBorderWidthBottom(2);
            separatorCell.setBorderColorBottom(PRIMARY_COLOR);
            separatorCell.setFixedHeight(5);
            separatorTable.addCell(separatorCell);
            document.add(separatorTable);

        } catch (Exception e) {
            throw new RuntimeException("Error adding header", e);
        }
    }

    private void addCustomerInfo(Document document, LiquidationWithDetailsDto liquidation) {
        try {
            Paragraph sectionTitle = new Paragraph("DATOS DEL CLIENTE", subtitleFont);
            sectionTitle.setSpacingBefore(15);
            sectionTitle.setSpacingAfter(10);
            document.add(sectionTitle);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 2});

            if (liquidation.getCustomer() != null) {
                addInfoRow(infoTable, "Cliente:", liquidation.getCustomer().getFirstName() + " " + liquidation.getCustomer().getLastName());
                addInfoRow(infoTable, "Documento:", liquidation.getCustomer().getIdDocumentType() + ": " + liquidation.getCustomer().getIdDocumentNumber());
                if (liquidation.getCustomer().getEmail() != null && liquidation.getCustomer().getEmail().isPresent()) {
                    addInfoRow(infoTable, "Email:", liquidation.getCustomer().getEmail().orElse("N/A"));
                }
                if (liquidation.getCustomer().getPhoneNumber() != null && liquidation.getCustomer().getPhoneNumber().isPresent()) {
                    addInfoRow(infoTable, "Teléfono:", liquidation.getCustomer().getPhoneNumber().orElse("N/A"));
                }
            }

            addInfoRow(infoTable, "Acompañantes:", String.valueOf(liquidation.getCompanion()));

            if (liquidation.getPaymentDeadline() != null) {
                addInfoRow(infoTable, "Válido hasta:", liquidation.getPaymentDeadline().format(DATE_FORMATTER));
            }

            addInfoRow(infoTable, "Tipo de cambio:", String.format("%.2f", liquidation.getCurrencyRate()));

            document.add(infoTable);

        } catch (Exception e) {
            throw new RuntimeException("Error adding customer info", e);
        }
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, boldFont));
        labelCell.setBorder(0);
        labelCell.setPaddingBottom(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", normalFont));
        valueCell.setBorder(0);
        valueCell.setPaddingBottom(5);
        table.addCell(valueCell);
    }

    private void addFlightServices(Document document, List<DFlightService> flightServices) {
        if (flightServices == null || flightServices.isEmpty()) return;

        try {
            Paragraph sectionTitle = new Paragraph("SERVICIOS DE VUELO", subtitleFont);
            sectionTitle.setSpacingBefore(20);
            sectionTitle.setSpacingAfter(10);
            document.add(sectionTitle);

            for (DFlightService service : flightServices) {
                if (service.getFlightBookings() != null && !service.getFlightBookings().isEmpty()) {
                    PdfPTable table = new PdfPTable(5);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{2, 2, 1.5f, 1.5f, 1.5f});
                    table.setSpacingAfter(10);

                    // Header
                    addTableHeader(table, "Ruta", "Aerolínea", "Fecha", "Precio", "Moneda");

                    // Data
                    for (DFlightBooking booking : service.getFlightBookings()) {
                        if (booking.getIsActive() == null || !booking.getIsActive()) continue;
                        String route = booking.getOrigin() + " → " + booking.getDestiny();
                        addTableCell(table, route);
                        addTableCell(table, booking.getAeroline());
                        addTableCell(table, booking.getDepartureDate() != null ? booking.getDepartureDate().format(DATE_FORMATTER) : "N/A");
                        addTableCell(table, String.format("%.2f", booking.getTotalPrice()));
                        addTableCell(table, booking.getCurrency() != null ? booking.getCurrency().name() : "PEN");
                    }

                    document.add(table);

                    // Tariff info
                    if (service.isTaxed()) {
                        Paragraph tariffInfo = new Paragraph(
                            String.format("Tarifa aplicada: %.2f%%", service.getTariffRate() * 100), 
                            smallFont
                        );
                        tariffInfo.setSpacingAfter(10);
                        document.add(tariffInfo);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error adding flight services", e);
        }
    }

    private void addHotelServices(Document document, List<DHotelService> hotelServices) {
        if (hotelServices == null || hotelServices.isEmpty()) return;

        try {
            Paragraph sectionTitle = new Paragraph("SERVICIOS DE HOTEL", subtitleFont);
            sectionTitle.setSpacingBefore(20);
            sectionTitle.setSpacingAfter(10);
            document.add(sectionTitle);

            for (DHotelService service : hotelServices) {
                if (service.getHotelBookings() != null && !service.getHotelBookings().isEmpty()) {
                    PdfPTable table = new PdfPTable(6);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{2, 1.5f, 1.5f, 1.5f, 1.5f, 1});
                    table.setSpacingAfter(10);

                    // Header
                    addTableHeader(table, "Hotel", "Habitación", "Check-in", "Check-out", "Precio/Noche", "Moneda");

                    // Data
                    for (DHotelBooking booking : service.getHotelBookings()) {
                        if (booking.getIsActive() == null || !booking.getIsActive()) continue;
                        addTableCell(table, booking.getHotel());
                        addTableCell(table, booking.getRoom());
                        addTableCell(table, booking.getCheckIn() != null ? booking.getCheckIn().format(DATE_FORMATTER) : "N/A");
                        addTableCell(table, booking.getCheckOut() != null ? booking.getCheckOut().format(DATE_FORMATTER) : "N/A");
                        addTableCell(table, String.format("%.2f", booking.getPriceByNight()));
                        addTableCell(table, booking.getCurrency() != null ? booking.getCurrency().name() : "PEN");
                    }

                    document.add(table);

                    // Tariff info
                    if (service.isTaxed()) {
                        Paragraph tariffInfo = new Paragraph(
                            String.format("Tarifa aplicada: %.2f%%", service.getTariffRate() * 100), 
                            smallFont
                        );
                        tariffInfo.setSpacingAfter(10);
                        document.add(tariffInfo);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error adding hotel services", e);
        }
    }

    private void addTourServices(Document document, List<DTourService> tourServices) {
        if (tourServices == null || tourServices.isEmpty()) return;

        try {
            Paragraph sectionTitle = new Paragraph("SERVICIOS DE TOUR", subtitleFont);
            sectionTitle.setSpacingBefore(20);
            sectionTitle.setSpacingAfter(10);
            document.add(sectionTitle);

            for (DTourService service : tourServices) {
                if (service.getTours() != null && !service.getTours().isEmpty()) {
                    PdfPTable table = new PdfPTable(6);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{2, 1.5f, 1.5f, 1.5f, 1.5f, 1});
                    table.setSpacingAfter(10);

                    // Header
                    addTableHeader(table, "Tour", "Lugar", "Inicio", "Fin", "Precio", "Moneda");

                    // Data
                    for (DTour tour : service.getTours()) {
                        if (tour.getIsActive() == null || !tour.getIsActive()) continue;
                        addTableCell(table, tour.getTitle());
                        addTableCell(table, tour.getPlace());
                        addTableCell(table, tour.getStartDate() != null ? tour.getStartDate().format(DATE_FORMATTER) : "N/A");
                        addTableCell(table, tour.getEndDate() != null ? tour.getEndDate().format(DATE_FORMATTER) : "N/A");
                        addTableCell(table, String.format("%.2f", tour.getPrice()));
                        addTableCell(table, tour.getCurrency() != null ? tour.getCurrency().name() : "PEN");
                    }

                    document.add(table);

                    // Tariff info
                    if (service.isTaxed()) {
                        Paragraph tariffInfo = new Paragraph(
                            String.format("Tarifa aplicada: %.2f%%", service.getTariffRate() * 100), 
                            smallFont
                        );
                        tariffInfo.setSpacingAfter(10);
                        document.add(tariffInfo);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error adding tour services", e);
        }
    }

    private void addAdditionalServices(Document document, List<DAdditionalServices> additionalServices) {
        if (additionalServices == null || additionalServices.isEmpty()) return;

        try {
            Paragraph sectionTitle = new Paragraph("SERVICIOS ADICIONALES", subtitleFont);
            sectionTitle.setSpacingBefore(20);
            sectionTitle.setSpacingAfter(10);
            document.add(sectionTitle);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1.5f, 1});
            table.setSpacingAfter(10);

            // Header
            addTableHeader(table, "Descripción", "Precio", "Moneda");

            // Data
            for (DAdditionalServices service : additionalServices) {
                if (service.getIsActive() == null || !service.getIsActive()) continue;
                addTableCell(table, "Servicio adicional");
                addTableCell(table, String.format("%.2f", service.getPrice()));
                addTableCell(table, service.getCurrency() != null ? service.getCurrency().name() : "PEN");
            }

            document.add(table);

        } catch (Exception e) {
            throw new RuntimeException("Error adding additional services", e);
        }
    }

    private void addTotals(Document document, LiquidationWithDetailsDto liquidation) {
        try {
            // Separator
            document.add(new Paragraph(" "));

            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(50);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.setSpacingBefore(20);

            // Total PEN
            PdfPCell labelPEN = new PdfPCell(new Phrase("Total (PEN):", boldFont));
            labelPEN.setBorder(0);
            labelPEN.setHorizontalAlignment(Element.ALIGN_RIGHT);
            labelPEN.setPaddingRight(10);
            totalsTable.addCell(labelPEN);

            PdfPCell valuePEN = new PdfPCell(new Phrase(String.format("S/. %.2f", liquidation.getTotalAmount()), boldFont));
            valuePEN.setBorder(0);
            valuePEN.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalsTable.addCell(valuePEN);

            // Commission PEN
            if (liquidation.getTotalCommissionPEN() != null && liquidation.getTotalCommissionPEN() > 0) {
                PdfPCell labelCommPEN = new PdfPCell(new Phrase("Comisión (PEN):", smallFont));
                labelCommPEN.setBorder(0);
                labelCommPEN.setHorizontalAlignment(Element.ALIGN_RIGHT);
                labelCommPEN.setPaddingRight(10);
                totalsTable.addCell(labelCommPEN);

                PdfPCell valueCommPEN = new PdfPCell(new Phrase(String.format("S/. %.2f", liquidation.getTotalCommissionPEN()), smallFont));
                valueCommPEN.setBorder(0);
                valueCommPEN.setHorizontalAlignment(Element.ALIGN_RIGHT);
                totalsTable.addCell(valueCommPEN);
            }

            // Total USD
            if (liquidation.getTotalAmountUSD() != null && liquidation.getTotalAmountUSD() > 0) {
                PdfPCell labelUSD = new PdfPCell(new Phrase("Total (USD):", boldFont));
                labelUSD.setBorder(0);
                labelUSD.setHorizontalAlignment(Element.ALIGN_RIGHT);
                labelUSD.setPaddingRight(10);
                labelUSD.setPaddingTop(5);
                totalsTable.addCell(labelUSD);

                PdfPCell valueUSD = new PdfPCell(new Phrase(String.format("$ %.2f", liquidation.getTotalAmountUSD()), boldFont));
                valueUSD.setBorder(0);
                valueUSD.setHorizontalAlignment(Element.ALIGN_RIGHT);
                valueUSD.setPaddingTop(5);
                totalsTable.addCell(valueUSD);

                // Commission USD
                if (liquidation.getTotalCommissionUSD() != null && liquidation.getTotalCommissionUSD() > 0) {
                    PdfPCell labelCommUSD = new PdfPCell(new Phrase("Comisión (USD):", smallFont));
                    labelCommUSD.setBorder(0);
                    labelCommUSD.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    labelCommUSD.setPaddingRight(10);
                    totalsTable.addCell(labelCommUSD);

                    PdfPCell valueCommUSD = new PdfPCell(new Phrase(String.format("$ %.2f", liquidation.getTotalCommissionUSD()), smallFont));
                    valueCommUSD.setBorder(0);
                    valueCommUSD.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    totalsTable.addCell(valueCommUSD);
                }
            }

            document.add(totalsTable);

        } catch (Exception e) {
            throw new RuntimeException("Error adding totals", e);
        }
    }

    private void addFooter(Document document, LiquidationWithDetailsDto liquidation) {
        try {
            document.add(new Paragraph(" "));

            // Terms
            Paragraph terms = new Paragraph("TÉRMINOS Y CONDICIONES", subtitleFont);
            terms.setSpacingBefore(30);
            terms.setSpacingAfter(10);
            document.add(terms);

            String termsText = """
                • Esta cotización tiene validez hasta la fecha indicada.
                • Los precios están sujetos a disponibilidad al momento de la reserva.
                • El tipo de cambio aplicado es referencial y puede variar.
                • Se requiere un anticipo del 50% para confirmar la reserva.
                • Política de cancelación: consultar con el asesor.
                """;

            Paragraph termsContent = new Paragraph(termsText, smallFont);
            termsContent.setSpacingAfter(20);
            document.add(termsContent);

            // Contact
            if (liquidation.getStaffOnCharge() != null) {
                Paragraph staffInfo = new Paragraph(
                    "Atendido por: " + liquidation.getStaffOnCharge().getUser().getUserName(),
                    normalFont
                );
                document.add(staffInfo);
            }

            // Footer line
            Paragraph footer = new Paragraph("PTC Travel Agency - Tu viaje, nuestra pasión", smallFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

        } catch (Exception e) {
            throw new RuntimeException("Error adding footer", e);
        }
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(PRIMARY_COLOR);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "N/A", normalFont));
        cell.setPadding(6);
        cell.setBorderColor(BORDER_COLOR);
        table.addCell(cell);
    }
}
