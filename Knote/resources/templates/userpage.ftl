<#-- @ftlvariable name="all" type="java.util.List<com.jstarczewski.knote.db.model.Note>" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Welcome">
    <div class="posts">
        <h3 class="content-subhead">Notes for you</h3>
        <@layout.notes_list notes=all></@layout.notes_list>
    </div>
</@layout.mainLayout>
