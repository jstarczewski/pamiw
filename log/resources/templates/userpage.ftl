<#-- @ftlvariable name="all" type="java.util.List<com.jstarczewski.log.publication.api.PdfResponse>" -->
<#-- @ftlvariable name="all_pubs" type="java.util.List<com.jstarczewski.log.publication.api.PublicationResponse>" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->

<#import "template.ftl" as layout />

<@layout.mainLayout title="Welcome">
    <div class="posts">
        <h3 class="content-subhead">Your files</h3>
        <@layout.pdfs_list_del pdfs_del=all></@layout.pdfs_list_del>
    </div>
    <div class="posts">
        <h3 class="content-subhead">Your publications</h3>
        <@layout.pubs_list pubs=all_pubs></@layout.pubs_list>
    </div>
</@layout.mainLayout>
