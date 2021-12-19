# Jenkins部署

## jenkin安装
``` shell
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install jenkins
```

- Setup Jenkins as a daemon launched on start. See /etc/init.d/jenkins for more details.
- Create a ‘jenkins’ user to run this service.
- jenkins运行logs：`/var/log/jenkins/jenkins.log`
- jenkins配置文件： `/etc/default/jenkins `