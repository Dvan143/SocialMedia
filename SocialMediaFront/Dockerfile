FROM nginx:1.29.0

COPY nginx/default.conf /etc/nginx/conf.d/default.conf
COPY html/ /usr/share/nginx/html/
COPY static/ /usr/share/nginx/html/

EXPOSE 80