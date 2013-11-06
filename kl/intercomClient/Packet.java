package intercomClient;

import java.awt.Image;

public class Packet {
	public static class Packet0Message { String message; }
	public static class Packet1Alert { }
	public static class Packet2Camera { int direction; }
	public static class Packet3Snapshot { Image image; }
	public static class Packet4VideoStream { }
	public static class Packet5AudioStream { }
}
