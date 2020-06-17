<#-- @ftlvariable name="error" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Welcome">
    <form class="pure-form-stacked" action="/register" method="post" enctype="application/x-www-form-urlencoded">
        <#if error??>
            <p class="error">${error}</p>
        </#if>

        <#if login??>
            <label for="login">Login
                <input type="text" name="login" id="login" value="${login}">
            </label>
        <#else >
            <label for="login">Login
                <input type="text" name="login" id="login" value="">
            </label>
        </#if>

        <label for="password">Password
            <input type="password" name="password" id="password">
        </label>

        <label for="repeat_password">Repeat password
            <input type="password" name="repeat_password" id="repeat_password">
        </label>

        <input class="pure-button pure-button-primary" type="submit" value="Register">
    </form>
</@layout.mainLayout>
