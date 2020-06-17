<#-- @ftlvariable name="note" type="com.jstarczewski.knote.db.model.Note" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Add publication">
    <form class="pure-form-stacked" action="/user/note" method="post" enctype="multipart/form-data">
        <#if error??>
            <p class="error">${error}</p>
        </#if>

        <label for="title">Title
            <textarea name="title" id="title" cols="80" rows="1"></textarea>
        </label>

        <label for="content">Content
            <textarea name="content" cols="80" rows="20" id="content"></textarea>
        </label>

        <label>
            <input id="public" type="checkbox" name="public" value="true">Public<br>
        </label>

        <input class="pure-button pure-button-primary" type="submit" value="Add">
    </form>
</@layout.mainLayout>