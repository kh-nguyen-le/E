package securityServer;

import java.awt.Image;

public class Packet {
	public static class Packet0Message { public String message; }
	public static class Packet1Alert { }
	public static class Packet2Motor { public boolean direction; }
	public static class Packet3Snapshot { public Image image; }
	public static class Packet4VideoStream { }
	public static class Packet5AudioStream { }
}
