import SimpleMindMap from 'https://cdn.jsdelivr.net/npm/simple-mind-map@0.10.2/dist/simpleMindMap.esm.min.js';
// import { marked } from 'https://cdn.jsdelivr.net/npm/marked/marked.min.js';
// import { DOMPurify } from 'https://cdnjs.cloudflare.com/ajax/libs/dompurify/3.0.6/purify.min.js';
import { marked } from "https://cdn.jsdelivr.net/npm/marked/lib/marked.esm.js";

export function initMindMap(container, mindData) {
  
  var mindMap = new SimpleMindMap({
    el: container,
    data: mindData.root,
    initRootNodePosition: ['left', 'center'],
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
