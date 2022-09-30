import moment from 'moment'
import html2canvas from 'html2canvas'
import JsPDF from 'jspdf'

export const getTime = function (time) {
  return time ? moment(time, 'YYYY-MM-DD') : null
}

// 禁用当前后面的时间
export const disabledDate = function (current) {
  return current && current > moment().endOf('day')
}

// 身份证数据
export const formatNumber = function (value) {
  value += ''
  const list = value.split('.')
  const prefix = list[0].charAt(0) === '-' ? '-' : ''
  let num = prefix ? list[0].slice(1) : list[0]
  return `${prefix}${num}${list[1] ? `.${list[1]}` : ''}`
}

// 数组对象属性比较大小
export const compareTime = function (property, desc) {
  return function (a, b) {
    var value1 = moment(a[property]).valueOf()
    var value2 = moment(b[property]).valueOf()
    if (desc === true) {
      // 升序排列
      return value1 - value2
    } else {
      // 降序排列
      return value2 - value1
    }
  }
}
export const compare = function (property, desc) {
  return function (a, b) {
    var value1 = Number(a[property])
    var value2 = Number(b[property])
    if (desc === true) {
      // 升序排列
      return value1 - value2
    } else {
      // 降序排列
      return value2 - value1
    }
  }
}

// 政治身份数据
export const politicaloutlookOptions = [
  {keyy: 1, valuee: '中国共产党党员'},
  {keyy: 2, valuee: '中国共产主义青年团团员'},
  {keyy: 3, valuee: '九三学社社员'},
  {keyy: 4, valuee: '中国农工民主党党员'},
  {keyy: 5, valuee: '中国民主同盟盟员'},
  {keyy: 6, valuee: '中国民主促进会成员'},
  {keyy: 7, valuee: '中国民主建国会成员'},
  {keyy: 8, valuee: '中国致公党党员'},
  {keyy: 9, valuee: '中国国民党革命委员会成员'},
  {keyy: 10, valuee: '群众'}
]

// 生成pdf
function downloadPDF (ele, pdfName) {
  window.scrollTo(0, 0)
  let eleW = ele.offsetWidth * 4 // 获得该容器的宽
  let eleH = ele.offsetHeight // 获得该容器的高
  let eleOffsetTop = ele.offsetTop // 获得该容器到文档顶部的距离
  let eleOffsetLeft = ele.offsetLeft // 获得该容器到文档最左的距离

  var canvas = document.createElement('canvas')
  var abs = 0

  let winIn = document.documentElement.clientWidth || document.body.clientWidth // 获得当前可视窗口的宽度（不包含滚动条）
  let winOut = window.innerWidth // 获得当前窗口的宽度（包含滚动条）

  if (winOut > winIn) {
    // abs = (win_o - win_i)/2;    // 获得滚动条长度的一半
    abs = (winOut - winIn) / 2 // 获得滚动条宽度的一半
    // console.log(a, '新abs');
  }
  canvas.width = eleW * 2 // 将画布宽&&高放大两倍
  canvas.height = eleH * 2

  var context = canvas.getContext('2d')
  context.scale(2, 2)
  // context.translate(-eleOffsetLeft - abs, -eleOffsetTop)
  // 这里默认横向没有滚动条的情况，因为offset.left(),有无滚动条的时候存在差值，因此
  // translate的时候，要把这个差值去掉

  // html2canvas(element).then( (canvas)=>{ //报错
  // html2canvas(element[0]).then( (canvas)=>{
  html2canvas (ele, {
    dpi: 300,
    height: ele.scrollHeight,
    width: ele.scrollWidth, // 为了使横向滚动条的内容全部展示，这里必须指定
    // allowTaint: true, // 允许 canvas 污染， allowTaint参数要去掉，否则是无法通过toDataURL导出canvas数据的
    useCORS: true // 允许canvas画布内 可以跨域请求外部链接图片, 允许跨域请求。
  }).then((canvas) => {
    var contentWidth = canvas.width
    var contentHeight = canvas.height
    // 一页pdf显示html页面生成的canvas高度;

    var pageHeight = contentWidth / 595.28 * 841.89
    // 未生成pdf的html页面高度
    var leftHeight = contentHeight
    // 页面偏移
    var position = 20
    // a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
    var imgWidth = 595.28
    var imgHeight = 595.28 / contentWidth * contentHeight
    var pageData = canvas.toDataURL('image/jpeg', 1.0)
    var pdf = new JsPDF ('', 'pt', 'a4')

    // 有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
    // 当内容未超过pdf一页显示的范围，无需分页
    console.log('处理完画布高度：' + contentHeight)
    console.log('每页高度：' + pageHeight)
    if (leftHeight < pageHeight) {
      // 在pdf.addImage(pageData, 'JPEG', 左，上，宽度，高度)设置在pdf中显示；
      pdf.addImage(pageData, 'JPEG', 0, 20, imgWidth, imgHeight)
      // pdf.addImage(pageData, 'JPEG', 20, 40, imgWidth, imgHeight);
    } else { // 分页
      while (leftHeight > 10) {
        console.log(leftHeight)
        pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
        leftHeight -= pageHeight
        position -= 841.89
        // 避免添加空白页
        if (leftHeight > 10) {
          pdf.addPage()
        }
      }
    }
    // 可动态生成
    pdf.save(pdfName)
  })
}
function downloadPDF1(element, pdfName) {
  // var element = document.getElementById("page");
  html2canvas(element, {
    logging:false
  }).then(function(canvas) {
    var pdf = new JsPDF('p', 'mm', 'a4');    //A4纸，纵向
    var ctx = canvas.getContext('2d'),
      a4w = 250, a4h = 277,    //A4大小，210mm x 297mm，四边各保留10mm的边距，显示区域190x277
      imgHeight = Math.floor(a4h * canvas.width / a4w),    //按A4显示比例换算一页图像的像素高度
      renderedHeight = 0;

    while(renderedHeight < canvas.height) {
      var page = document.createElement("canvas");
      page.width = canvas.width;
      page.height = Math.min(imgHeight, canvas.height - renderedHeight);//可能内容不足一页

      //用getImageData剪裁指定区域，并画到前面创建的canvas对象中
      page.getContext('2d').putImageData(ctx.getImageData(0, renderedHeight, canvas.width, Math.min(imgHeight, canvas.height - renderedHeight)), 0, 0);
      pdf.addImage(page.toDataURL('image/jpeg', 1.0), 'JPEG', 10, 10, a4w, Math.min(a4h, a4w * page.height / page.width));    //添加图像到页面，保留10mm边距

      renderedHeight += imgHeight;
      if(renderedHeight < canvas.height)
        pdf.addPage();//如果后面还有内容，添加一个空页
      page = null;
    }
    pdf.save(pdfName);
  });
}

async function previewImg (ele) {
  window.scrollTo(0, 0)
  let eleW = ele.offsetWidth // 获得该容器的宽
  let eleH = ele.offsetHeight // 获得该容器的高
  let eleOffsetTop = ele.offsetTop // 获得该容器到文档顶部的距离
  let eleOffsetLeft = ele.offsetLeft // 获得该容器到文档最左的距离

  var canvas = document.createElement('canvas')
  var abs = 0

  let winIn = document.documentElement.clientWidth || document.body.clientWidth // 获得当前可视窗口的宽度（不包含滚动条）
  let winOut = window.innerWidth // 获得当前窗口的宽度（包含滚动条）

  if (winOut > winIn) {
    abs = (winOut - winIn) / 2 // 获得滚动条宽度的一半
  }
  canvas.width = eleW * 2
  canvas.height = eleH * 2

  var context = canvas.getContext('2d')
  context.scale(2, 2)
  context.translate(-eleOffsetLeft - abs, -eleOffsetTop)
  var baseSrc = ''
  await html2canvas (ele, {
    dpi: 300,
    useCORS: true, // 允许canvas画布内 可以跨域请求外部链接图片, 允许跨域请求。
    // allowTaint: true
  }).then((canvas) => {
    var pageData = canvas.toDataURL('image/jpeg', 1.0)
    baseSrc = pageData
  })

  return baseSrc
}

export {
  downloadPDF,
  downloadPDF1,
  previewImg
}
