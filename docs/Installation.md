# Mongo DB

1. Install [MongoDB Community Edition](https://docs.mongodb.com/manual/installation/)

# Install Java Services on Ubuntu

## Compile and build the code

1.  Download the code from [GitHub](https://github.com/Joindbre?tab=repositories)
2.  Use [MVN](https://maven.apache.org/download.cgi) to compile and build the code as a standalone jar

## Install and Launch the Plugin

1.  Create a file replacing **name** with the service name

```
vim /etc/systemd/system/{name}.service
```

3.  Add the following content to the file
    -   Replace **name**, **user**, and **description** accordingly

```
[Unit]
Description={description}
After=syslog.target

[Service]
User={user}
ExecStart=/var/name/{name}.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

5.  Enable the service:

```
systemctl enable {name}.service
```

7.  Launch the service:

```
systemctl start {name}.service
```

or

```
service {name} start
```

11.  Check that the service has been started correctly

```
systemctl status {name}.service
```

