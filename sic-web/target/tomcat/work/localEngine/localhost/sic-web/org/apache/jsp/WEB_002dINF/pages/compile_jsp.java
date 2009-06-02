package org.apache.jsp.WEB_002dINF.pages;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import si.fri.spo.sic.action.CompileBean;

public final class compile_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fform_005fbeanclass;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ffile_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fsubmit_005fname;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fs_005fform_005fbeanclass = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ffile_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fsubmit_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fs_005fform_005fbeanclass.release();
    _005fjspx_005ftagPool_005fs_005ffile_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005fsubmit_005fname.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html\n");
      out.write("PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n");
      out.write("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
      out.write("\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
      out.write("\n");
      out.write("<title>SIC/XE Assembler web POC</title>\n");
      out.write("\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\t");
      //  s:form
      net.sourceforge.stripes.tag.FormTag _jspx_th_s_005fform_005f0 = (net.sourceforge.stripes.tag.FormTag) _005fjspx_005ftagPool_005fs_005fform_005fbeanclass.get(net.sourceforge.stripes.tag.FormTag.class);
      _jspx_th_s_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_s_005fform_005f0.setParent(null);
      // /WEB-INF/pages/compile.jsp(17,1) name = beanclass type = java.lang.Object reqTime = true required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_s_005fform_005f0.setBeanclass(CompileBean.class);
      int[] _jspx_push_body_count_s_005fform_005f0 = new int[] { 0 };
      try {
        int _jspx_eval_s_005fform_005f0 = _jspx_th_s_005fform_005f0.doStartTag();
        if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
          if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = _jspx_page_context.pushBody();
            _jspx_push_body_count_s_005fform_005f0[0]++;
            _jspx_th_s_005fform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
            _jspx_th_s_005fform_005f0.doInitBody();
          }
          do {
            out.write("\n");
            out.write("\t\t<div>\n");
            out.write("\t\t\t");
            if (_jspx_meth_s_005ffile_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context, _jspx_push_body_count_s_005fform_005f0))
              return;
            out.write("\n");
            out.write("\t\t\t");
            if (_jspx_meth_s_005fsubmit_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context, _jspx_push_body_count_s_005fform_005f0))
              return;
            out.write("\n");
            out.write("\t\t</div>\n");
            out.write("\t");
            int evalDoAfterBody = _jspx_th_s_005fform_005f0.doAfterBody();
            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
              break;
          } while (true);
          if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
            out = _jspx_page_context.popBody();
            _jspx_push_body_count_s_005fform_005f0[0]--;
          }
        }
        if (_jspx_th_s_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
          return;
        }
      } catch (Throwable _jspx_exception) {
        while (_jspx_push_body_count_s_005fform_005f0[0]-- > 0)
          out = _jspx_page_context.popBody();
        _jspx_th_s_005fform_005f0.doCatch(_jspx_exception);
      } finally {
        _jspx_th_s_005fform_005f0.doFinally();
        _005fjspx_005ftagPool_005fs_005fform_005fbeanclass.reuse(_jspx_th_s_005fform_005f0);
      }
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_s_005ffile_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_s_005fform_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:file
    net.sourceforge.stripes.tag.InputFileTag _jspx_th_s_005ffile_005f0 = (net.sourceforge.stripes.tag.InputFileTag) _005fjspx_005ftagPool_005fs_005ffile_005fname_005fnobody.get(net.sourceforge.stripes.tag.InputFileTag.class);
    _jspx_th_s_005ffile_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005ffile_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /WEB-INF/pages/compile.jsp(19,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffile_005f0.setName("sicSrc");
    int[] _jspx_push_body_count_s_005ffile_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_s_005ffile_005f0 = _jspx_th_s_005ffile_005f0.doStartTag();
      if (_jspx_th_s_005ffile_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_005ffile_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_005ffile_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_005ffile_005f0.doFinally();
      _005fjspx_005ftagPool_005fs_005ffile_005fname_005fnobody.reuse(_jspx_th_s_005ffile_005f0);
    }
    return false;
  }

  private boolean _jspx_meth_s_005fsubmit_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, PageContext _jspx_page_context, int[] _jspx_push_body_count_s_005fform_005f0)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:submit
    net.sourceforge.stripes.tag.InputSubmitTag _jspx_th_s_005fsubmit_005f0 = (net.sourceforge.stripes.tag.InputSubmitTag) _005fjspx_005ftagPool_005fs_005fsubmit_005fname.get(net.sourceforge.stripes.tag.InputSubmitTag.class);
    _jspx_th_s_005fsubmit_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fsubmit_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /WEB-INF/pages/compile.jsp(20,3) name = name type = null reqTime = true required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fsubmit_005f0.setName("assemble");
    int[] _jspx_push_body_count_s_005fsubmit_005f0 = new int[] { 0 };
    try {
      int _jspx_eval_s_005fsubmit_005f0 = _jspx_th_s_005fsubmit_005f0.doStartTag();
      if (_jspx_eval_s_005fsubmit_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_s_005fsubmit_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_push_body_count_s_005fsubmit_005f0[0]++;
          _jspx_th_s_005fsubmit_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_s_005fsubmit_005f0.doInitBody();
        }
        do {
          out.write("Prevedi");
          int evalDoAfterBody = _jspx_th_s_005fsubmit_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_s_005fsubmit_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
          _jspx_push_body_count_s_005fsubmit_005f0[0]--;
        }
      }
      if (_jspx_th_s_005fsubmit_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_005fsubmit_005f0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_005fsubmit_005f0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_005fsubmit_005f0.doFinally();
      _005fjspx_005ftagPool_005fs_005fsubmit_005fname.reuse(_jspx_th_s_005fsubmit_005f0);
    }
    return false;
  }
}
