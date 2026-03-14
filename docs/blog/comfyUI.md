---
title: ComfyUI 使用 记录
description: comfyui
hide:
  - navigation
tags:
  - Comfyui
bcs:
  分类: /tags/#tag:Docker
---
节点玩法：https://space.bilibili.com/515231056

---

### **一、ComfyUI 安装**
```bash
# 使用python3.12环境
pyenv virtualenv 3.12.2 ComfyUI-New
# 获取comfyui程序
git clone https://github.com/Comfy-Org/ComfyUI && cd ComfyUI
# 固定版本
git checkout v0.17.0
pyenv local ComfyUI-New
# （n卡）安装pytorch
pip install torch torchvision torchaudio --extra-index-url https://download.pytorch.org/whl/cu130
# 安装comfyui程序依赖
pip install -r requirements.txt
# 安装comfyui-manager程序依赖
pip install -r manager_requirements.txt
# 手动安装缺失库（可选-后续更新节点时也会自动安装）
python -m pip install --upgrade pip
pip install matrix-nio numba opencv-python opencv-contrib-python matplotlib piexif py-cpuinfo segment_anything nvidia-ml-py deepdiff simpleeval git+https://github.com/facebookresearch/sam2 
# 修改局域网权限
vim user/__manager/config.ini
#network_mode = public
network_mode = personal_cloud

# 启动程序
python main.py --enable-manager --listen
```

---

### **二、xxx**

