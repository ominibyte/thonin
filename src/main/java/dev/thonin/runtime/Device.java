package dev.thonin.runtime;

public class Device {
    private String id;          //A unique id for this device. We could use MAC address
    private long memory;        //The amount of memory currently available
    private short processors;   //The amount of processors available to be used
    private long diskSpace;     //The amount of diskspace left of the device
    private String arch;        //The system architecture
    private DeviceType deviceType;
    private Feature[] features; //The features available on this device

    private static Device device;

    private Device(){

    }

    public static Device getInstance(){
        if( device == null )
            device = new Device();
        return device;
    }

    public String getId() {
        return id;
    }

    public long getMemory() {
        return memory;
    }

    public short getProcessors() {
        return processors;
    }

    public long getDiskSpace() {
        return diskSpace;
    }

    public String getArch() {
        return arch;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
