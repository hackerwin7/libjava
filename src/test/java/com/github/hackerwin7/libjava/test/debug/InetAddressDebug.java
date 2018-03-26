//package com.github.hackerwin7.libjava.test.debug;
//
//import sun.net.spi.nameservice.NameService;
//import sun.net.spi.nameservice.NameServiceDescriptor;
//import sun.security.action.GetPropertyAction;
//
//import java.io.IOException;
//import java.net.*;
//import java.security.AccessController;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.ServiceLoader;
//
///**
// * Created by IntelliJ IDEA.
// * User: hackerwin7
// * Date: 2018/03/23
// * Time: 7:35 PM
// * Desc:
// */
//public class InetAddressDebug {
//
//    static InetAddressImpl impl;
//    static List<NameService> nameServices;
//
//    public static void main(String[] args) throws Exception {
//        impl = InetAddressImplFactory.create();
//        String provider = null;;
//        String propPrefix = "sun.net.spi.nameservice.provider.";
//        int n = 1;
//        nameServices = new ArrayList<NameService>();
//        provider = AccessController.doPrivileged(
//                new GetPropertyAction(propPrefix + n));
//        System.out.println("provider = " + provider);
//        while (provider != null) {
//            NameService ns = createNSProvider(provider);
//            if (ns != null)
//                nameServices.add(ns);
//
//            n++;
//            provider = AccessController.doPrivileged(
//                    new GetPropertyAction(propPrefix + n));
//            System.out.println("provider = " + provider);
//        }
//
//        // if not designate any name services provider,
//        // create a default one
//        if (nameServices.size() == 0) {
//            NameService ns = createNSProvider("default");
//            nameServices.add(ns);
//        }
//
//        System.out.println(nameServices);
//
//
//        InetAddress addr = InetAddress.getByName("10.12.194.82");
//        for (NameService nameService : nameServices) {
//            String host = nameService.getHostByAddr(addr.getAddress());
//            System.out.println("host = " + host);
//        }
//    }
//
//    private static NameService createNSProvider(String provider) {
//        if (provider == null)
//            return null;
//
//        NameService nameService = null;
//        if (provider.equals("default")) {
//            // initialize the default name service
//            nameService = new NameService() {
//                public InetAddress[] lookupAllHostAddr(String host)
//                        throws UnknownHostException {
//                    return impl.lookupAllHostAddr(host);
//                }
//                public String getHostByAddr(byte[] addr)
//                        throws UnknownHostException {
//                    return impl.getHostByAddr(addr);
//                }
//            };
//        } else {
//            final String providerName = provider;
//            try {
//                nameService = java.security.AccessController.doPrivileged(
//                        new java.security.PrivilegedExceptionAction<NameService>() {
//                            public NameService run() {
//                                Iterator<NameServiceDescriptor> itr =
//                                        ServiceLoader.load(NameServiceDescriptor.class)
//                                                .iterator();
//                                while (itr.hasNext()) {
//                                    NameServiceDescriptor nsd = itr.next();
//                                    if (providerName.
//                                            equalsIgnoreCase(nsd.getType()+","
//                                                                     +nsd.getProviderName())) {
//                                        try {
//                                            return nsd.createNameService();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                            System.err.println(
//                                                    "Cannot create name service:"
//                                                            +providerName+": " + e);
//                                        }
//                                    }
//                                }
//
//                                return null;
//                            }
//                        }
//                );
//            } catch (java.security.PrivilegedActionException e) {
//            }
//        }
//
//        return nameService;
//    }
//
//    interface InetAddressImpl {
//
//        String getLocalHostName() throws UnknownHostException;
//        InetAddress[]
//        lookupAllHostAddr(String hostname) throws UnknownHostException;
//        String getHostByAddr(byte[] addr) throws UnknownHostException;
//
//        InetAddress anyLocalAddress();
//        InetAddress loopbackAddress();
//        boolean isReachable(InetAddress addr, int timeout, NetworkInterface netif,
//                            int ttl) throws IOException;
//    }
//
//    static class InetAddressImplFactory {
//
//        static InetAddressImpl create() {
////            return loadImpl(isIPv6Supported() ?
////                                                "Inet6AddressImpl" : "Inet4AddressImpl");
//            return loadImpl("Inet4AddressImpl");
//        }
//
//        static native boolean isIPv6Supported();
//    }
//
//    static InetAddressImpl loadImpl(String implName) {
//        Object impl = null;
//
//        /*
//         * Property "impl.prefix" will be prepended to the classname
//         * of the implementation object we instantiate, to which we
//         * delegate the real work (like native methods).  This
//         * property can vary across implementations of the java.
//         * classes.  The default is an empty String "".
//         */
//        String prefix = AccessController.doPrivileged(
//                new GetPropertyAction("impl.prefix", ""));
//        try {
//            impl = Class.forName("java.net." + prefix + implName).newInstance();
//        } catch (ClassNotFoundException e) {
//            System.err.println("Class not found: java.net." + prefix +
//                                       implName + ":\ncheck impl.prefix property " +
//                                       "in your properties file.");
//        } catch (InstantiationException e) {
//            System.err.println("Could not instantiate: java.net." + prefix +
//                                       implName + ":\ncheck impl.prefix property " +
//                                       "in your properties file.");
//        } catch (IllegalAccessException e) {
//            System.err.println("Cannot access class: java.net." + prefix +
//                                       implName + ":\ncheck impl.prefix property " +
//                                       "in your properties file.");
//        }
//
//        if (impl == null) {
//            try {
//                impl = Class.forName(implName).newInstance();
//            } catch (Exception e) {
//                throw new Error("System property impl.prefix incorrect");
//            }
//        }
//
//        return (InetAddressImpl) impl;
//    }
//
//    static class Inet4AddressImpl implements InetAddressImpl {
//        public native String getLocalHostName() throws UnknownHostException;
//        public native InetAddress[]
//        lookupAllHostAddr(String hostname) throws UnknownHostException;
//        public native String getHostByAddr(byte[] addr) throws UnknownHostException;
//        private native boolean isReachable0(byte[] addr, int timeout, byte[] ifaddr, int ttl) throws IOException;
//
//        public synchronized InetAddress anyLocalAddress() {
//            if (anyLocalAddress == null) {
//                anyLocalAddress = new Inet4Address(); // {0x00,0x00,0x00,0x00}
//                anyLocalAddress.holder().hostName = "0.0.0.0";
//            }
//            return anyLocalAddress;
//        }
//
//        public synchronized InetAddress loopbackAddress() {
//            if (loopbackAddress == null) {
//                byte[] loopback = {0x7f,0x00,0x00,0x01};
//                loopbackAddress = new Inet4Address("localhost", loopback);
//            }
//            return loopbackAddress;
//        }
//
//        public boolean isReachable(InetAddress addr, int timeout, NetworkInterface netif, int ttl) throws IOException {
//            byte[] ifaddr = null;
//            if (netif != null) {
//          /*
//           * Let's make sure we use an address of the proper family
//           */
//                java.util.Enumeration<InetAddress> it = netif.getInetAddresses();
//                InetAddress inetaddr = null;
//                while (!(inetaddr instanceof Inet4Address) &&
//                        it.hasMoreElements())
//                    inetaddr = it.nextElement();
//                if (inetaddr instanceof Inet4Address)
//                    ifaddr = inetaddr.getAddress();
//            }
//            return isReachable0(addr.getAddress(), timeout, ifaddr, ttl);
//        }
//        private InetAddress      anyLocalAddress;
//        private InetAddress      loopbackAddress;
//    }
//}
