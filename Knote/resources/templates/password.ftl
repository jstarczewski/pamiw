<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="userId" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Welcome">
    <form class="pure-form-stacked" action="/user/password" method="post" enctype="application/x-www-form-urlencoded">
        <#if error??>
            <p class="error">${error}</p>
        </#if>

        <label for="old_password">Old password
            <input type="password" name="old_password" id="old_password">
        </label>

        <label for="new_password">New password
            <input type="password" name="new_password" id="new_password">
        </label>

        <label for="repeat_password">Repeat new password
            <input type="password" name="repeat_password" id="repeat_password">
        </label>

        <input class="pure-button pure-button-primary" type="submit" value="Change">
    </form>
</@layout.mainLayout>
