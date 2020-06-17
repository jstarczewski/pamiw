<#-- @ftlvariable name="all" type="java.util.List<com.jstarczewski.knote.db.model.Note>" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Log in to access private notes">
    <#if all??>
        <div class="posts">
            <h3 class="content-subhead">Public notes</h3>
            <@layout.notes_list notes=all></@layout.notes_list>
        </div>
    </#if>
</@layout.mainLayout>
