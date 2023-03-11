修改文件：nano /etc/kernel/cmdline
在第一行末尾添加：quiet amd_iommu=on
添加模块：/etc/modules
vfio
vfio_iommu_type1
vfio_pci
vfio_virqfd
update-initramfs -u -k all
proxmox-boot-tool refresh

重启

直通m2 ref:https://foxi.buduanwang.vip/virtualization/1754.html/
ls -la /dev/disk/by-id/|grep -v dm|grep -v lvm|grep -v part
qm set 102 --scsi0 /dev/disk/by-id/nvme-ZHITAI_TiPlus5000_512GB_ZTA2512KA22422018K
qm set 102 --scsi1 /dev/disk/by-id/ata-LITEON_CV3-8D256_0028023003AD

echo 0000:00:0a.0 > /sys/bus/pci/drivers/vfio-pci/unbind



echo "options vfio-pci ids=10de:2486,10de:228b" > /etc/modprobe.d/vfio.conf

修改本機磁盤容量
https://www.youtube.com/watch?v=LuCXHHc2u18

lvremove /dev/pve/data
lvresize -l +100%FREE /dev/pve/root
resize2fs /dev/mapper/pve-root

顯卡直通
https://www.youtube.com/watch?v=5ce-CcYjqe8
https://www.youtube.com/watch?v=BoMlfk397h0&t=915s

https://www.youtube.com/watch?v=_JTEsQufSx4
https://gitlab.com/risingprismtv/single-gpu-passthrough/-/wikis/1)-Preparations

更新：
nano /etc/apt/sources.list.d/pve-enterprise.list 註釋掉
# deb https://enterprise.proxmox.com/debian/pve stretch pve-enterprise
echo "deb http://download.proxmox.com/debian/pve stretch pve-no-subscription" > /etc/apt/sources.list.d/pve-install-repo.list

gpg --keyserver keyserver.ubuntu.com --recv-keys 0D9A1950E2EF0603
gpg --export --armor 0D9A1950E2EF0603 | apt-key add -
apt update

echo "options vfio-pci ids=10de:2486,10de:228b  disable_vga=1" > /etc/modprobe.d/vfio.conf
nano /etc/modprobe.d/blacklist.conf
blacklist nouveau
blacklist radeon
blacklist nvidia

nano /etc/modprobe.d/kvm.conf
options kvm ignore_msrs=1

update-initramfs -u

reboot


直通：
1. https://blog.51cto.com/u_12242014/2382885

win10

{
	itemId: 'thermal',
	colspan: 2,
	printBar: false,
	title: gettext('温度'),
	textField: 'sensors_json',
	renderer: function(value) {
		value = value.replace(/temp([0-9]{1,})_input/g,'input');
		// Intel
		if (value.indexOf("coretemp-isa") != -1 ) {
			value = value.replace(/coretemp-isa-(.{4})/g,'coretemp-isa');
			value = value.replace(/nct6798-isa-(.{4})/g,'nct6798-isa');
			value = JSON.parse(value);
			try {var cpu_Intel = 'CPU: ' + value['coretemp-isa']['Package id 0']['input'].toFixed(1) + '°C';} catch(e) {var cpu_Intel = '';} 
			try {var acpi = ' || 主板:  ' + value['acpitz-acpi-0']['temp1']['input'].toFixed(1) + '°C';} catch(e) {var acpi = '';} 
			try {var pch = ' || 南桥:  ' + value['pch_cometlake-virtual-0']['temp1']['input'].toFixed(1) + '°C';} catch(e) {var pch = '';} 
			try {var pci0 = ' || 网卡:  ' + value['nct6798-isa']['PECI Agent 0']['input'].toFixed(1) + '°C';} catch(e) {var pci0 = '';}
			// 如果存在主板、PCI网卡温度,优先显示
			if (cpu_Intel.length > 0 && pch.length + acpi.length + pci0.length > 0) {
				return `${cpu_Intel}${acpi}${pch}${pci0}`;
			// 如果不存在,显示 CPU 全核温度,最高支持 8 核心
			} else if (cpu_Intel.length > 0) {
				try {var cpu0 = ' || 核心 0 : ' + value['coretemp-isa']['Core 0']['input'].toFixed(1) + '°C';} catch(e) {var cpu0 = '';} 
				try {var cpu1 = ' | 核心 1 : ' + value['coretemp-isa']['Core 1']['input'].toFixed(1) + '°C';} catch(e) {var cpu1 = '';} 
				try {var cpu2 = ' | 核心 2 : ' + value['coretemp-isa']['Core 2']['input'].toFixed(1) + '°C';} catch(e) {var cpu2 = '';} 
				try {var cpu3 = ' | 核心 3 : ' + value['coretemp-isa']['Core 3']['input'].toFixed(1) + '°C';} catch(e) {var cpu3 = '';} 
				try {var cpu4 = ' | 核心 4 : ' + value['coretemp-isa']['Core 4']['input'].toFixed(1) + '°C';} catch(e) {var cpu4 = '';} 
				try {var cpu5 = ' | 核心 5 : ' + value['coretemp-isa']['Core 5']['input'].toFixed(1) + '°C';} catch(e) {var cpu5 = '';} 
				try {var cpu6 = ' | 核心 6 : ' + value['coretemp-isa']['Core 6']['input'].toFixed(1) + '°C';} catch(e) {var cpu6 = '';} 
				try {var cpu7 = ' | 核心 7 : ' + value['coretemp-isa']['Core 7']['input'].toFixed(1) + '°C';} catch(e) {var cpu7 = '';} 
				return `${cpu_Intel}${cpu0}${cpu1}${cpu2}${cpu3}${cpu4}${cpu5}${cpu6}${cpu7}`;
			} 
		// AMD
		} else if (value.indexOf("amdgpu-pci") != -1 ) {
			value = value.replace(/k10temp-pci-(.{4})/g,'k10temp-pci');
			value = value.replace(/zenpower-pci-(.{4})/g,'zenpower-pci');
			value = value.replace(/amdgpu-pci-(.{4})/g,'amdgpu-pci');
			value = JSON.parse(value);
			try {var cpu_amd_k10 = 'CPU: ' + value['k10temp-pci']['Tctl']['input'].toFixed(1) + '°C';} catch(e) {var cpu_amd_k10 = '';} 
			try {var cpu_amd_zen = 'CPU: ' + value['zenpower-pci']['Tctl']['input'].toFixed(1) + '°C';} catch(e) {var cpu_amd_zen = '';} 
			try {var amdgpu = ' | GPU:  ' + value['amdgpu-pci']['edge']['input'].toFixed(1) + '°C';} catch(e) {var amdgpu = '';} 
			return `${cpu_amd_k10}${cpu_amd_zen}${amdgpu}`;
		} else {
			return `提示: CPU 及 主板 温度读取异常`;
		}
	}
},
{		
	itemId: 'nvme_ssd',
	colspan: 2,
	printBar: false,
	title: gettext('NVME'),
	textField: 'smartctl_nvme_json',
	renderer: function(value) {
		value = JSON.parse(value);
		if (value['model_name']) {
			try {var model_name = value['model_name'];} catch(e) {var model_name = '';} 
			try {var percentage_used = ' | 使用寿命: ' + value['nvme_smart_health_information_log']['percentage_used'].toFixed(0) + '% ';} catch(e) {var percentage_used = '';} 
			try {var data_units_read = value['nvme_smart_health_information_log']['data_units_read']*512/1024/1024/1024;var data_units_read = '(读: ' + data_units_read.toFixed(2) + 'TB, ';} catch(e) {var data_units_read = '';} 
			try {var data_units_written = value['nvme_smart_health_information_log']['data_units_written']*512/1024/1024/1024;var data_units_written = '写: ' + data_units_written.toFixed(2) + 'TB)';} catch(e) {var data_units_written = '';} 
			try {var power_on_time = ' | 通电: ' + value['power_on_time']['hours'].toFixed(0) + '小时';} catch(e) {var power_on_time = '';} 
			try {var temperature = ' | 温度: ' + value['temperature']['current'].toFixed(1) + '°C';} catch(e) {var temperature = '';} 
			return `${model_name}${percentage_used}${data_units_read}${data_units_written}${power_on_time}${temperature}`;
		} else { 
			return `提示: 未安装硬盘或已直通硬盘控制器`;
		}
	}
},
{
	itemId: 'SATA_sda',
	colspan: 2,
	printBar: false,
	title: gettext('SATA_sda'),
	textField: 'smartctl_sda_json',
	renderer: function(value) {
		if (value.indexOf("Device is in STANDBY mode") != -1 ) {
			return `提示: 磁盘休眠中`;
		} else if (value.indexOf("No such device") != -1 ) {
			return `提示: 未安装硬盘或已直通硬盘控制器`;
		} else {
		value = JSON.parse(value);
			try {var model_name = value['model_name'];} catch(e) {var model_name = '';} 
			try {var user_capacity = value['user_capacity']['bytes']/1024/1024/1024;var user_capacity = ' | 容量: ' + user_capacity.toFixed(2) + ' GB';} catch(e) {var user_capacity = '';} 
			try {var power_on_time = ' | 已通电: ' + value['power_on_time']['hours'].toFixed(0) + ' 小时';} catch(e) {var power_on_time = '';} 
			try {var error_count = value['ata_smart_error_log']['summary']['count'].toFixed(0);if (error_count != 0){error_count = ' | 磁盘错误: ' + error_count;} else {var error_count = '';} } catch(e) {var error_count = '';} 
			try {var self_count = value['ata_smart_self_test_log']['standard']['count'].toFixed(0);if (self_count != 0){self_count = ' | 自检错误: ' + self_count;} else {var self_count = '';} } catch(e) {var self_count = '';} 
			try {var temperature = ' | 温度: ' + value['temperature']['current'].toFixed(1) + '°C';} catch(e) {var temperature = '';} 
			return `${model_name}${user_capacity}${power_on_time}${error_count}${self_count}${temperature}`;
		}
	}
},
{
	itemId: 'SATA_sdb',
	colspan: 2,
	printBar: false,
	title: gettext('SATA_sdb'),
	textField: 'smartctl_sdb_json',
	renderer: function(value) {
		if (value.indexOf("Device is in STANDBY mode") != -1 ) {
			return `提示: 磁盘休眠中`;
		} else if (value.indexOf("No such device") != -1 ) {
			return `提示: 未安装硬盘或已直通硬盘控制器`;
		} else {
		value = JSON.parse(value);
			try {var model_name = value['model_name'];} catch(e) {var model_name = '';} 
			try {var user_capacity = value['user_capacity']['bytes']/1024/1024/1024;var user_capacity = ' | 容量: ' + user_capacity.toFixed(2) + ' GB';} catch(e) {var user_capacity = '';} 
			try {var power_on_time = ' | 已通电: ' + value['power_on_time']['hours'].toFixed(0) + ' 小时';} catch(e) {var power_on_time = '';} 
			try {var error_count = value['ata_smart_error_log']['summary']['count'].toFixed(0);if (error_count != 0){error_count = ' | 磁盘错误: ' + error_count;} else {var error_count = '';} } catch(e) {var error_count = '';} 
			try {var self_count = value['ata_smart_self_test_log']['standard']['count'].toFixed(0);if (self_count != 0){self_count = ' | 自检错误: ' + self_count;} else {var self_count = '';} } catch(e) {var self_count = '';} 
			try {var temperature = ' | 温度: ' + value['temperature']['current'].toFixed(1) + '°C';} catch(e) {var temperature = '';} 
			return `${model_name}${user_capacity}${power_on_time}${error_count}${self_count}${temperature}`;
		}
	}
},
{
	itemId: 'MHz',
	colspan: 2,
	printBar: false,
	title: gettext('CPU频率'),
	textField: 'cpusensors',
	renderer:function(value){
		var f0 = value.match(/CPU MHz.*?([\d]+)/)[1];
		var f1 = value.match(/CPU min MHz.*?([\d]+)/)[1];
		var f2 = value.match(/CPU max MHz.*?([\d]+)/)[1];
		return `实时: ${f0} MHz || 最小: ${f1} MHz | 最大: ${f2} MHz `
	}
},


version: '3'

networks:
  rustdesk-net:
    external: false

services:
  hbbs:
    container_name: hbbs
    ports:
      - 21115:21115
      - 21116:21116
      - 21116:21116/udp
      - 21118:21118
    image: rustdesk/rustdesk-server:latest
    command: hbbs -r 192.168.0.109:21117
    volumes:
      - ./data:/root
    networks:
      - rustdesk-net
    depends_on:
      - hbbr
    restart: unless-stopped

  hbbr:
    container_name: hbbr
    ports:
      - 21117:21117
      - 21119:21119
    image: rustdesk/rustdesk-server:latest
    command: hbbr
    volumes:
      - ./data:/root
    networks:
      - rustdesk-net
    restart: unless-stopped

docker run -d \
  --name=ghs \
  -e PUID=$UID \
  -e PGID=$GID \
  -p 8096:8096 \
  -p 8920:8920 \
  -p 7359:7359/udp \
  -p 1900:1900/udp \
  -v /data/docker/jellyfin/ghs/config:/config \
  -v /mnt/docker-wd/docker/cloudTorrent/downloads:/data/video \
  --restart unless-stopped \
  nyanmisaka/jellyfin:latest