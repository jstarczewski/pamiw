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
                            <li class="nav-item"><a class="pure-button" href="/logout">Log out</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user">homepage</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user/upload">upload</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user/upload/pub">Add pub</a></li>
                        <#else>
                            <li class="nav-item"><a class="pure-button" href="/login">Log in</a></li>
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
<#-- @ftlvariable name="pdf" type="com.jstarczewski.log.publication.api.PdfResponse" -->
    <section class="post">
        <header class="post-header">
            <p class="post-meta">
                <a href="/user/${pdf.id}">${pdf.fileName}</a>
                by <a href="/user/">${pdf.author}</a>
            </p>
        </header>
    </section>
</#macro>

<#macro pdf_li_del pdf>
<#-- @ftlvariable name="pdf" type="com.jstarczewski.log.publication.api.PdfResponse" -->
    <section class="post">
        <header class="post-header">
            <p class="post-meta">
                <a href="/user/${pdf.id}">${pdf.fileName}</a>
                by <a href="/user/">${pdf.author}</a>
                <a class="pure-button pure-button-primary" href="user/pdf/delete/${pdf.id}">Delete</a>
            </p>
        </header>
    </section>
</#macro>

<#macro pub_li pub>
<#-- @ftlvariable name="pub" type="com.jstarczewski.log.publication.api.PublicationResponse" -->
    <section class="post">
        <header class="post-header">
            <p class="post-meta">
                <a>${pub.title}</a> by <a>${pub.author}</a>
            </p>
            <div class="post-description">${pub.description}</div>
            <#if pub.pdfs??>
                <div class="posts">
                    <h3 class="content-subhead">Linked files</h3>
                    <@pdfs_list pdfs=pub.pdfs></@pdfs_list>
                </div>
            </#if>
            <#if error??>
                <p class="error">${error}</p>
            </#if>
            <form class="pure-form-stacked" action="/user/pub/link/${pub.id}" method="post"
                  enctype="application/x-www-form-urlencoded">
                <label for="fileName">Link file by name
                    <input type="text" name="fileName" id="fileName">
                </label>
                <input class="pure-button pure-button-primary" type="submit" value="Link file">
            </form>
            <form class="pure-form-stacked" action="/user/pub/unlink/${pub.id}" method="post"
                  enctype="application/x-www-form-urlencoded">
                <label for="fileName">Unink file by name
                    <input type="text" name="fileName" id="fileName">
                </label>
                <input class="pure-button pure-button-primary" type="submit" value="Unlink file">
            </form>
            <p>
                <a class="pure-button pure-button-primary" href="user/pub/delete/${pub.id}">Delete</a>
            </p>
        </header>
    </section>
</#macro>

<#macro pdfs_list pdfs>
    <ul>
        <#list pdfs as pdf>
            <@pdf_li pdf=pdf></@pdf_li>
        <#else>
            <li>There are no linked pdfs yet</li>
        </#list>
    </ul>
</#macro>

<#macro pdfs_list_del pdfs_del>
    <ul>
        <#list pdfs_del as pdf>
            <@pdf_li_del pdf=pdf></@pdf_li_del>
        <#else>
            <li>There are no pdfs yet</li>
        </#list>
    </ul>
</#macro>

<#macro pubs_list pubs>
    <ul>
        <#list pubs as pub>
            <@pub_li pub=pub></@pub_li>
        <#else>
            <li>There are no publications yet</li>
        </#list>
    </ul>
</#macro>
