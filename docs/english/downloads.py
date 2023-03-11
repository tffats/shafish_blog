# -*- coding:utf-8 -*-
import os
import ssl
import requests  # 导入requests包
from bs4 import BeautifulSoup
import urllib
import progressbar
import sys


pbar = None
ssl._create_default_https_context = ssl._create_unverified_context
source_path = 'D:\\BaiduNetdiskDownload\\IDM_DATA\\file\\'
url = 'https://downloads.freemdict.com/Language_Learning_Videos/%E8%8B%B1%E8%AF%AD/English%20Speaking%20Course%20Slang_%20Idioms_%20Pronunciation%20%28ESL%29/'


def mkdir(path):
    # dir_url_path = urllib.parse.unquote(dir_url.split('/')[-2], 'utf-8')
    # path = path + dir_url_path
    # 去除首位空格
    path = path.strip()
    # 去除尾部 \ 符号
    path = path.rstrip("\\")
    # 判断路径是否存在
    isExists = os.path.exists(path)
    # 判断结果
    if not isExists:
        # 如果不存在则创建目录,创建目录操作函数
        '''
        os.mkdir(path)与os.makedirs(path)的区别是,当父目录不存在的时候os.mkdir(path)不会创建，os.makedirs(path)则会创建父目录
        '''
        # 此处路径最好使用utf-8解码，否则在磁盘中可能会出现乱码的情况 .decode('utf-8')
        os.makedirs(path)
        print(path + ' create success')
        return True
    else:
        # 如果目录存在则不创建，并提示目录已存在
        # print(path + ' exist,create fail')
        return False


def _progress(block_num, block_size, total_size):
    '''回调函数
    @block_num: 已经下载的数据块
    @block_size: 数据块的大小
    @total_size: 远程文件的大小
    '''
    global pbar
    if pbar is None:
        pbar = progressbar.ProgressBar(maxval=total_size)
        pbar.start()

    downloaded = block_num * block_size
    if downloaded < total_size:
        pbar.update(downloaded)
    else:
        pbar.finish()
        pbar = None


def download(dir_name, dir_url):
    sub_strhtml = requests.get(dir_url)
    sub_soup = BeautifulSoup(sub_strhtml.text, 'lxml')
    for child in sub_soup.find_all('a'):
        href = dir_url+child['href']
        # 避免child.text文字过长出现..&gt;跟window文件命名冲突，还好这个网站的child['href']和child.text是一致的，可以直接用不费事
        name = urllib.parse.unquote(child['href'], 'utf-8')

        if child.text == '../' or child.text == 'fmdl_readme.md':
            continue
        if href.endswith('/'):  # 如果是目录，继续递归
            mkdir(dir_name+name)
            download(dir_name+name, href)
        elif not os.path.exists(dir_name+name):  # 如果是文件且不存在，就直接下载
            print(name+' downloading')
            # 处理发生异常时暂停下载的情况（网络波动等）
            do_download(href, dir_name+name, 3)
    print("The contents of the current directory have been downloaded")


def do_download(link, save_path, retry_num):
    attempts = 0
    success = False
    while attempts < retry_num and not success:
        try:
            urllib.request.urlretrieve(
                link, save_path, _progress)
            success = True
        except Exception as e:
            print("Retrying...")
            # 可以加个断点下载，原理：先获取已下载文件大小，再在请求头中设置对应区间下载
            if os.path.exists(save_path):
                os.remove(save_path)
            attempts += 1
            if attempts == retry_num:
                # 随便抛个异常退出程序
                raise RuntimeError('bibubibu:send email')


download(source_path, url)