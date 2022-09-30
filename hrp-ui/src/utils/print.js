export default function (el, config, css) {

  let iframe = document.getElementById("print-iframe");
  console.log(iframe, "000011111")


  if (!iframe) {
    let _el = document.querySelector(el);
    iframe = document.createElement("IFRAME");
    let doc = null;
    iframe.setAttribute("id", "print-iframe");
    iframe.setAttribute("style", "display: none");
    document.body.appendChild(iframe);
    doc = iframe.contentWindow.document;
    doc.title = "2345"

    // 可以引入自定义样式
    let st = `<style type="text/css" media="print">`
    st=st+`@page
      {
          size: ${config.size ? config.size : "auto"};   /* auto is the initial value */
          margin: ${config.headerAndFooter ? "12.5mm" : " 12.5mm 10mm"};  /* this affects the margin in the printer settings */
          page-break-after: auto;
      }`

    st= st+`
    html
    {
        background-color: none;
        margin: 0px;  /* this affects the margin on the html before sending to printer */
    }

    body
    {
        margin: 0; /* margin you want for the content */
    }`;
    if(css)
    {
      st=st+css;
    }
    console.log("css",css);
    st= st+`</style>`;
    doc.write(st);
    console.log("-",_el, 999999);
    doc.write(_el.innerHTML);
    doc.close();
    // iframe.contentWindow.focus()
  }
  iframe.contentWindow.print();
  document.body.removeChild(iframe);
}
