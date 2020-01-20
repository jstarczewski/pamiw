<#-- @ftlvariable name="pdf" type="com.jstarczewski.log.publication.api.PdfResponse" -->
<#-- @ftlvariable name="pub" type="com.jstarczewski.log.publication.api.PublicationResponse" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Add publication">
    <script type="text/javascript" src="main.js"></script>
    <form class="pure-form-stacked" action="/user/upload/pub" method="post" enctype="multipart/form-data">
        <#if error??>
            <p class="error">${error}</p>
        </#if>

        <label for="title">Title
            <textarea name="title" id="title" cols="80" rows="1"></textarea>
        </label>

        <label for="description">Description
            <textarea name="description" cols="80" rows="20" id="description"></textarea>
        </label>

        <input class="pure-button pure-button-primary" type="submit" id="addPubBt" value="Add">
    </form>
</@layout.mainLayout>