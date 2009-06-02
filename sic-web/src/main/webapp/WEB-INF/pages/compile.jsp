<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib 
prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%><%@page 
import="si.fri.spo.sic.action.CompileBean"%>

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>SIC/XE Assembler web POC</title>

</head>
<body>
	<s:form beanclass="<%=CompileBean.class%>">
		<div>
			<s:file name="sicSrc"/>
			<s:submit name="assemble">Prevedi</s:submit>
		</div>
	</s:form>
</body>
</html>