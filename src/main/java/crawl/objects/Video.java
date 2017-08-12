package crawl.objects;

public class Video {
	private static final int STRING_TERMS = 11;
	private String channel = "";
	private String channelSubs = "";
	private String date = "";
	private String description = "";
	private String dislikes = "";
	private String duration = "";
	private String likes = "";
	private String tags = "";
	private String thumbnail = "";
	private String title = "";
	private String type = "";
	private String views = "";
	private String videoId = "";
	
	public static int getStringTerms() {
		return STRING_TERMS;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = this.channel + channel;
	}
	public String getChannelSubs() {
		return channelSubs;
	}
	public void setChannelSubs(String channelSubs) {
		this.channelSubs = this.channelSubs + channelSubs;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = this.date + date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = this.description + description;
	}
	public String getDislikes() {
		return dislikes;
	}
	public void setDislikes(String dislikes) {
		this.dislikes = this.dislikes + dislikes;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = this.likes +likes;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = this.tags + tags;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = this.thumbnail + thumbnail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = this.title + title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = this.views + views;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = this.videoId + videoId;
	}
}
