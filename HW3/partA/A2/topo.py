"""
Example topology of Quagga routers
"""

import inspect
import os
from mininext.topo import Topo
from mininext.services.quagga import QuaggaService

from collections import namedtuple

QuaggaHost = namedtuple("QuaggaHost", "name ip DG loIP")
net = None


class QuaggaTopo(Topo):

    "Creates a topology of Quagga routers"

    def __init__(self):
        """Initialize a Quagga topology with 4 routers and 2 hosts, configure their IP
           addresses, loop back interfaces, and paths to their private
           configuration directories."""
        Topo.__init__(self)

        # Directory where this file / script is located"
        selfPath = os.path.dirname(os.path.abspath(
            inspect.getfile(inspect.currentframe())))  # script directory

        # Initialize a service helper for Quagga with default options
        quaggaSvc = QuaggaService(autoStop=False)

        # Path configurations for mounts
        quaggaBaseConfigPath = selfPath + '/configs/'

        # List of Quagga host configs
        quaggaHosts = []
        quaggaHosts.append(QuaggaHost(name='H1', ip='170.1.1.1/24',DG='via 170.1.1.2',loIP=None))
        quaggaHosts.append(QuaggaHost(name='R1', ip='170.1.1.2/24',DG='',loIP=None))
        quaggaHosts.append(QuaggaHost(name='R2', ip='171.1.1.2/24',DG='',loIP=None))
        quaggaHosts.append(QuaggaHost(name='R3', ip='172.1.1.2/24',DG='',loIP=None))
        quaggaHosts.append(QuaggaHost(name='R4', ip='175.7.7.1/24',DG='',loIP=None))
        quaggaHosts.append(QuaggaHost(name='H2', ip='175.7.7.2/24',DG='via 175.7.7.1',loIP=None))

        hostList = []
        # Setup each Quagga router, add a link between it and the IXP fabric
        for host in quaggaHosts:
            # Create an instance of a host, called a quaggaContainer
            quaggaContainer = self.addHost(name=host.name,
                                           ip=host.ip,
                                           hostname=host.name,
                                           defaultRoute=host.DG,
                                           privateLogDir=True,
                                           privateRunDir=True,
                                           inMountNamespace=True,
                                           inPIDNamespace=True,
                                           inUTSNamespace=True)
            hostList.append(quaggaContainer)
            # Add a loopback interface with an IP in router's announced range
            self.addNodeLoopbackIntf(node=host.name, ip=host.loIP)
            # Configure and setup the Quagga service for this node
            quaggaSvcConfig ={'quaggaConfigPath': quaggaBaseConfigPath + host.name}
            self.addNodeService(node=host.name, service=quaggaSvc,nodeConfig=quaggaSvcConfig)

        self.addLink(hostList[0], hostList[1])#H1-R1
        self.addLink(hostList[1], hostList[2])#R1-R2
        self.addLink(hostList[1], hostList[3])#R1-R3
        self.addLink(hostList[5], hostList[4])#H2-R4
        self.addLink(hostList[2], hostList[4])#R2-R4
        self.addLink(hostList[3], hostList[4])#R3-R4
        
