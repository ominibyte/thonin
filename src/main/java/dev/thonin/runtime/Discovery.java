package dev.thonin.runtime;

/**
 * This class would be used to discover services on the network and advertise services using mdns
 * As mdns is used for the local network, this means that discoveries can be made from
 * End Devices -> Edge Device
 * Edge Device -> Fog Device (Possibly)
 * Communicating with other levels require other protocols like CoAP. This means that Each App needs to have a config
 * file where the IP address would be configured, using a custom DSL which we can make compatible with a subset of TOSCA
 */
public class Discovery {
}
