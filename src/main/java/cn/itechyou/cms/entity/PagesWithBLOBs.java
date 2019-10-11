package cn.itechyou.cms.entity;

public class PagesWithBLOBs extends Pages {
    private String mdContent;

    private String htmlContent;

    public String getMdContent() {
		return mdContent;
	}

	public void setMdContent(String mdContent) {
		this.mdContent = mdContent;
	}

	public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent == null ? null : htmlContent.trim();
    }
}