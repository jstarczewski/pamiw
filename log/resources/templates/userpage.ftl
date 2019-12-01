<#-- @ftlvariable name="all" type="java.util.List<io.ktor.samples.kweet.model.Kweet>" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Welcome">
    <div class="posts">
        <h3 class="content-subhead">Your files</h3>
        <@layout.pdfs_list pdfs=all></@layout.pdfs_list>
    </div>
</@layout.mainLayout>
