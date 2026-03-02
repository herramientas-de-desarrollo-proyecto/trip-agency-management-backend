server {
    listen 80;
    server_name ptc-app.gonzalogtz.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name ptc-app.gonzalogtz.com;

    ssl_certificate /etc/letsencrypt/live/ptc-app.gonzalogtz.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/ptc-app.gonzalogtz.com/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    location / {
        proxy_pass http://127.0.0.1:3090;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
