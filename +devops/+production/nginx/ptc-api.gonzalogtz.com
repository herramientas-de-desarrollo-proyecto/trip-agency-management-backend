server {
    listen 80;
    server_name ptc-api.gonzalogtz.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name ptc-api.gonzalogtz.com;

    ssl_certificate /etc/letsencrypt/live/ptc-api.gonzalogtz.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/ptc-api.gonzalogtz.com/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    client_max_body_size 50M;

    location / {
        proxy_pass http://127.0.0.1:3091;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
