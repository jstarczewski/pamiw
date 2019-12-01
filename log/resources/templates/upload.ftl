<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="userId" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Welcome">
    <form class="pure-form-stacked" action="/user/upload" method="post" enctype="multipart/form-data">
        <#if error??>
            <p class="error">${error}</p>
        </#if>

        <label for="filename">File name
            <input type="text" name="title" id="title">
        </label>

        <label for="file">File
            <input type="file" name="file" id="file">
        </label>

        <input class="pure-button pure-button-primary" type="submit" value="Upload">
    </form>
</@layout.mainLayout>
