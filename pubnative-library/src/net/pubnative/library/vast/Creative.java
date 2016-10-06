package net.pubnative.library.vast;

import java.util.ArrayList;

import org.droidparts.annotation.serialize.XML;
import org.droidparts.model.Model;

public class Creative extends Model {
	private static final long serialVersionUID = 1L;

	@XML(attribute = "id")
	public long id;
	@XML(attribute = "AdID")
	public long adId;
	@XML(attribute = "sequence")
	public int sequesnce;
	@XML(tag = "Linear" + XML.SUB + "Duration")
	public String duration;

	@XML(tag = "Linear" + XML.SUB + "VideoClicks" + XML.SUB + "ClickThrough")
	public String videoClickUrl;

	@XML(tag = "Linear" + XML.SUB + "TrackingEvents", attribute = "Tracking")
	public ArrayList<Creative.TrackingEvent> trackingEvents;
	@XML(tag = "Linear" + XML.SUB + "MediaFiles", attribute = "MediaFile")
	public ArrayList<Creative.MediaFile> mediaFiles;

	//

	public static class TrackingEvent extends Model {
		private static final long serialVersionUID = 1L;

		@XML(attribute = "event")
		public String event;
		@XML
		public String url;
	}

	public static class MediaFile extends Model {
		private static final long serialVersionUID = 1L;

		@XML(attribute = "id")
		public long id;
		@XML(attribute = "bitrate")
		public int bitrate;
		@XML(attribute = "delivery")
		public String delivery;
		@XML(attribute = "height")
		public int height;
		@XML(attribute = "maintainAspectRatio")
		public boolean maintainAspectRatio;
		@XML(attribute = "scalable")
		public boolean scalable;
		@XML(attribute = "type")
		public String type;
		@XML(attribute = "width")
		public int width;

		@XML
		public String url;
	}
}