<#-- @ftlvariable name="user" type="com.jstarczewski.knote.db.UserTile" -->

<#macro mainLayout title="Welcome">
    <!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>${title} | Knote</title>
        <link rel="stylesheet" type="text/css" href="/styles/main.css">
    </head>
    <body>
    <div class="pure-g">
        <div class="sidebar pure-u-1 pure-u-md-1-4">
            <div class="header">
                <div class="brand-title">Knote</div>
                <nav class="nav">
                    <ul class="nav-list">
                        <#if user??>
                            <li class="nav-item"><a class="pure-button" href="/logout">Log out</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user">homepage</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user/note">add note</a></li>
                            <li class="nav-item"><a class="pure-button" href="/user/password">change credentials</a>
                            </li>
                        <#else>
                            <li class="nav-item"><a class="pure-button" href="/login">log in</a></li>
                            <li class="nav-item"><a class="pure-button" href="/register">register</a></li>
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

<#macro note_li note>
<#-- @ftlvariable name="notes" type="com.jstarczewski.knote.db.model.Note" -->
    <section class="post">
        <header class="post-header">
            <p class="post-meta">
                <a>${note.title}</a> by <a>${note.author}</a>
            </p>
            <div class="post-description">${note.content}</div>
            <#if note?? && user?? && note.author == user.login>
                <a class="pure-button pure-button-primary" href="user/note/delete/${note.id}">Delete</a>
            <#else >
            </#if>
        </header>
    </section>
</#macro>

<#macro notes_list notes>
    <ul>
        <#list notes as note>
            <@note_li note=note></@note_li>
        <#else>
            <li>There are no notes yet</li>
        </#list>
    </ul>
</#macro>
