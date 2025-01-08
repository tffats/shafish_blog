---
title: tts
tags:
  - F5-TTS
hide:
  - navigation
---

## F5-tts 

### 安装

``` shell
pyenv install -v 3.10.6
pyenv virtualenv 3.10.6 tts
git clone https://github.com/SWivid/F5-TTS.git
cd F5-TTS
pyenv local tts
# 配置http代理
# export http_proxy="http://192.168.0.109:10001"
# export https_proxy=$http_proxy
pip install -e .
```

### 控制台

``` shell
f5-tts_infer-gradio --port 7860 --host 0.0.0.0
```

date = %{A1: .config/polybar/modules/polybar-calendar/calendar.sh:}%A, %d %B%{A}