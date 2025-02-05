1. Create fileto use ssh without password
   129.174.10.65
ssh -oHostKeyAlgorithms=+ssh-dss pmaraver@cng.gmu.edu   
   Yn2c-4HN-D1Tt-oUp87
sweng: u3ixWwxBsTj4Itp5
Patricia0205^
123ixWwx34

https://aws.cec.gmu.edu
cng.gmu.edu:8080
NMember
nmpass58

##

3.210.2.30

## Steps to  install the integration with LiterMate
1. Install mongoDB: https://www.mongodb.com/docs/manual/tutorial/install-mongodb-on-red-hat/

sudo yum install -y mongodb-org-3.5 mongodb-org-database-3.5 mongodb-org-server-3.5 mongodb-mongosh-3.5 mongodb-org-mongos-3.5 mongodb-org-tools-3.5


## Linux utils
- Check linux version
```
lsb_release -a
```


ssh tail -f /var/log/messages


/etc/ssh/sshd_config

sudo service sshd restart


142  export LD_LIBRARY_PATH=/opt/glibc-2.14/lib


## SOLR
/opt/solr/server/logs
solr version 6.6.0

java version "1.8.0_202"

## init d

/etc/init.d/

## Check status of all processes

service --status-all


## SSH 

remove known host client
```
ssh-keygen -R cng.gmu.edu
```
restart ssh
service sshd restart
config

vi /etc/ssh/sshd_config 