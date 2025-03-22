import SimpleMindMap from 'https://cdn.jsdelivr.net/npm/simple-mind-map@0.10.2/dist/simpleMindMap.esm.min.js';
// import { marked } from 'https://cdn.jsdelivr.net/npm/marked/marked.min.js';
// import { DOMPurify } from 'https://cdnjs.cloudflare.com/ajax/libs/dompurify/3.0.6/purify.min.js';
import { marked } from "https://cdn.jsdelivr.net/npm/marked/lib/marked.esm.js";

export function initMindMap(container, mindData) {
  
  var mindMap = new SimpleMindMap({
    el: container,
    data: mindData.root,
    initRootNodePosition: ['left'],
    watermarkConfig: {
      text: 'shafish.cn',
      lineSpacing: 300,
      textSpacing: 100,
      angle: 30,
      textStyle: {
          color: '#999',
          opacity: 0.5,
          fontSize: 14
      }
    },
    mousewheelAction: 'move',// zoom（放大缩小）、move（上下移动）
    // 当mousewheelAction设为move时，可以通过该属性控制鼠标滚动一下视图移动的步长，单位px
    mousewheelMoveStep: 100,
    // 鼠标缩放是否以鼠标当前位置为中心点，否则以画布中心点
    mouseScaleCenterUseMousePosition: true,
    // 当mousewheelAction设为zoom时，或者按住Ctrl键时，默认向前滚动是缩小，向后滚动是放大，如果该属性设为true，那么会反过来
    mousewheelZoomActionReverse: true,
    // 禁止鼠标滚轮缩放，你仍旧可以使用api进行缩放
    disableMouseWheelZoom: false,
    // 连线的粗细
    lineWidth: 3,
    // 连线的颜色
    lineColor: '#549688',
    // 连线样式
    lineDasharray: 'none',
    // 连线风格，支持三种
    // 1.曲线（curve）。仅logicalStructure、mindMap、verticalTimeline三种结构支持。
    // 2.直线（straight）。
    // 3.直连（direct）。仅logicalStructure、mindMap、organizationStructure、verticalTimeline四种结构支持。
    lineStyle: 'straight', 
    // 曲线连接时，根节点和其他节点的连接线样式保持统一，默认根节点为 ( 型，其他节点为 { 型，设为true后，都为 { 型。仅logicalStructure、mindMap两种结构支持。
    rootLineKeepSameInCurve: true,
    // 直线连接(straight)时，连线的圆角大小，设置为0代表没有圆角，仅支持logicalStructure、mindMap、verticalTimeline三种结构
    lineRadius: 5,
    // 连线尾部是否显示标记，目前只支持箭头
    showLineMarker: false,
    // 概要连线的粗细
    generalizationLineWidth: 1,
    // 节点支持自由拖拽
    enableFreeDrag: false,
    customNoteContentShow: {
      show: (content, left, top, node) => {
        // bus.emit('showNoteContent', [content, left, top, node])
        console.log('备注：', content);
        const popup = document.getElementById('custom-note-popup');
        const contentBody = popup.querySelector('.content-body');
        
        // 解析 Markdown
        const cleanHtml = marked.parse(content || '暂无内容'); // DOMPurify.sanitize();
        // 设置弹窗内容
        contentBody.innerHTML = cleanHtml;
        // 定位到鼠标右侧（偏移 10px）
        popup.style.left = `${left + 10}px`;
        popup.style.top = `${top}px`;
        popup.classList.remove('hidden');

        // 自动滚动到顶部
        contentBody.scrollTo(0, 0);
      },
      hide: () => {
        // bus.emit('hideNoteContent')
      }
    }
  });
  // mindMap.setThemeConfig({
  //   backgroundColor: '#1b1d1e'
  // })
}
