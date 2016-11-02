# Apache mod_proxy and Spring Boot

## Install mod_proxy and other required package

    sudo apt-get install libapache2-mod-proxy-html libxml2-dev

## Activate

    a2enmod proxy proxy_http

## Edit `sites-enabled/000-default.conf`

    ProxyPass /bootlog http://localhost:8080/bootlog
    PRoxyPass /bootlog/* http://localhost:8080/bootlog
    ProxyPassReverse /bootlog http://localhost:8080/bootlog

    ServerName localhost
