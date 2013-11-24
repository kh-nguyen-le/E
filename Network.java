package securitySystem;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	static public final int port = 27960;
	
	static public void register (EndPoint endPoint) {
                Kryo kryo = endPoint.getKryo();
                kryo.register(MessagePacket.class);
                kryo.register(AlertPacket.class);
                kryo.register(MotorPacket.class);
                kryo.register(SnapshotPacket.class);
                kryo.register(AuthenticationPacket.class);
                kryo.register(HandshakePacket.class);
                kryo.register(SettingsPacket.class);
	}
	
        public static class MessagePacket { public String message; }
	public static class AlertPacket { public boolean alarmOn  = false; }
	public static class MotorPacket { public boolean direction; }
	public static class SnapshotPacket { public boolean alert; }
	public static class AuthenticationPacket { }
	public static class HandshakePacket {public boolean success = false; }
	public static class SettingsPacket { }
}
