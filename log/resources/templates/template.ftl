<#-- @ftlvariable name="user" type="com.jstarczewski.log.db.User" -->

<#macro mainLayout title="Welcome">
    <!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>${title} | PDFS</title>
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/grids-responsive-min.css">
        <link rel="stylesheet" type="text/css" href="/styles/main.css">
    </head>
    <body>
    <div class="pure-g">
        <div class="sidebar pure-u-1 pure-u-md-1-4">
            <div class="header">
                <div class="brand-title">PDFS</div>
                <nav class="nav">
                    <ul class="nav-list">
                        <#if user??>
                            <li class="nav-item"><a class="pure-button" href="/logout">sign out</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user">homepage</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user/upload">upload</a></li>
                        <#else>
                            <li class="nav-item"><a class="pure-button" href="/login">sign in</a></li>
                            <li class="nav-item"><a class="pure-button" href="/">homepage</a></li>
                        </#if>
                    </ul>
                </nav>
            </div>
        </div>

        <div class="content pure-u-1 pure-u-md-3-4">
            <h2>${title}</h2>
            <#nested />
        </div>
    </div>
    </body>
    </html>

</#macro>

<#macro pdf_li pdf>
<#-- @ftlvariable name="pdf" type="com.jstarczewski.log.db.Pdf" -->
    <section class="post">
        <header class="post-header">
            <p class="post-meta">
                <a href="/user/${pdf.id}">${pdf.fileName}</a>
                by <a href="/user/">${pdf.author}</a></p>
        </header>
    </section>
</#macro>

<#macro pdfs_list pdfs>
    <ul>
        <#list pdfs as pdf>
            <@pdf_li pdf=pdf></@pdf_li>
        <#else>
            <li>There are no pdfs yet</li>
        </#list>
    </ul>
</#macro>
