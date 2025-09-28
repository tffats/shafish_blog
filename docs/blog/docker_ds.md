---
title: 容器命令（AI总结）
description: linux, docker
hide:
  - navigation
tags:
  - Docker
---

在 Docker 中，`docker run` 是启动容器的核心命令，通过它可以配置容器的运行环境、资源限制、网络设置等。以下是 **Docker 容器的完整使用指南**，涵盖常用参数、示例场景及最佳实践：

---

### **一、基础命令格式**
```bash
docker run [选项] <镜像名称> [命令] [参数]
```

---

### **二、核心参数与使用场景**

#### **1. 容器交互模式**
- **`-i` (交互模式)**：保持标准输入（STDIN）打开。
- **`-t` (分配伪终端)**：为容器分配一个伪终端（TTY）。
- **组合使用 `-it`**：进入容器的交互式 Shell。
  ```bash
  docker run -it ubuntu /bin/bash  # 启动并进入 Ubuntu 容器的 Bash
  ```

#### **2. 容器后台运行**
- **`-d` (后台运行)**：容器以后台模式运行。
  ```bash
  docker run -d nginx  # 后台启动 Nginx 容器
  ```

#### **3. 端口映射**
- **`-p <宿主机端口>:<容器端口>`**：将容器端口映射到宿主机。
  ```bash
  docker run -d -p 8080:80 nginx  # 宿主机的 8080 端口映射到容器的 80 端口
  ```

#### **4. 数据卷挂载**
- **`-v <宿主机路径>:<容器路径>`**：挂载宿主机目录到容器。
  ```bash
  docker run -v /host/data:/app/data myapp  # 挂载宿主机目录到容器内的 /app/data
  ```

#### **5. 环境变量**
- **`-e <变量名>=<值>`**：设置容器内的环境变量。
  ```bash
  docker run -e MYSQL_ROOT_PASSWORD=123456 mysql  # 设置 MySQL 的 root 密码
  ```

#### **6. 容器命名**
- **`--name <容器名称>`**：为容器指定唯一名称。
  ```bash
  docker run --name my_nginx -d nginx  # 启动并命名为 my_nginx
  ```

#### **7. 自动重启策略**
- **`--restart=<策略>`**：定义容器退出时的重启策略（如 `always`、`on-failure`）。
  ```bash
  docker run --restart=always -d redis  # 容器异常退出时自动重启
  ```

#### **8. 资源限制**
- **内存限制**：`--memory=1g`（限制为 1GB）。
- **CPU 限制**：`--cpus=2`（最多使用 2 个 CPU 核心）。
  ```bash
  docker run --memory=1g --cpus=2 myapp  # 限制内存和 CPU
  ```

---

### **三、进阶配置**

#### **1. 网络模式**
- **`--network=<模式>`**：指定容器的网络模式（如 `host`、`bridge`、`none`）。
  ```bash
  docker run --network=host nginx  # 使用宿主机网络（直接暴露端口）
  ```

#### **2. 容器用户权限**
- **`--user=<用户名/UID>`**：指定容器内进程的运行用户。
  ```bash
  docker run --user=1000 myapp  # 以 UID 1000 的用户运行
  ```

#### **3. 只读文件系统**
- **`--read-only`**：将容器的根文件系统设为只读（需配合 `-v` 写入数据）。
  ```bash
  docker run --read-only -v /app/tmp myapp  # 仅允许写入 /app/tmp
  ```

#### **4. 日志驱动**
- **`--log-driver=<驱动>`**：指定日志记录方式（如 `json-file`、`syslog`）。
  ```bash
  docker run --log-driver=json-file --log-opt max-size=10m myapp  # 限制日志大小
  ```

---

### **四、实战示例**

#### **场景 1：运行 Web 服务并挂载配置**
```bash
docker run -d \
  --name my_web \
  -p 80:80 \
  -v /host/nginx.conf:/etc/nginx/nginx.conf \
  nginx
```

#### **场景 2：调试容器并进入 Shell**
```bash
docker run -it --rm \
  --name debug_tool \
  alpine sh  # 启动临时容器并进入 Shell，退出后自动删除（--rm）
```

#### **场景 3：连接其他容器（容器间通信）**
```bash
# 启动 MySQL 容器
docker run -d --name mysql_db -e MYSQL_ROOT_PASSWORD=123456 mysql

# 启动应用容器并连接到 MySQL
docker run -d \
  --name my_app \
  --link mysql_db:db \
  -e DB_HOST=db \
  myapp
```

---

### **五、最佳实践**
1. **精简镜像**：优先使用官方镜像或 Alpine 等轻量级基础镜像。
2. **单进程容器**：一个容器只运行一个主进程（如 Nginx、MySQL）。
3. **日志管理**：限制日志大小，避免磁盘占满。
4. **健康检查**：通过 `HEALTHCHECK` 指令监控容器状态。
5. **资源限制**：生产环境必须限制内存和 CPU，防止资源耗尽。

---

### **六、常用命令参考**
| 命令                          | 说明                          |
|-------------------------------|-------------------------------|
| `docker ps`                   | 查看运行中的容器              |
| `docker logs <容器名>`        | 查看容器日志                  |
| `docker exec -it <容器名> sh` | 进入运行中的容器执行命令      |
| `docker stop <容器名>`        | 停止容器                      |
| `docker rm <容器名>`          | 删除容器                      |

---

通过合理组合参数，可以高效管理容器的生命周期、资源分配和数据持久化。建议结合 Docker Compose 或 Kubernetes 进一步简化多容器编排。

### compose更新镜像

``` shell
# Pull latest version
docker compose pull

# Stop and remove older version
docker compose down

# Start the container
docker compose up -d 
```