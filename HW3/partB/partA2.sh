H1 ip route add 175.7.7.0/24 via 170.1.1.2 dev H1-eth0
H1 ip route add 171.1.1.0/24 via 170.1.1.2 dev H1-eth0
H1 ip route add 172.1.1.0/24 via 170.1.1.2 dev H1-eth0
H1 ip route add 173.1.1.0/24 via 170.1.1.2 dev H1-eth0
H1 ip route add 174.1.1.0/24 via 170.1.1.2 dev H1-eth0

R1 ip route add 175.7.7.0/24 via 171.1.1.2 dev R1-eth1
R1 ip route add 175.7.7.0/24 via 172.1.1.2 dev R1-eth2
R1 ip route add 173.1.1.0/24 via 171.1.1.2 dev R1-eth1
R1 ip route add 174.1.1.0/24 via 172.1.1.2 dev R1-eth2

R2 ip route add 175.7.7.0/24 via 173.1.1.1 dev R2-eth1
R2 ip route add 170.1.1.0/24 via 171.1.1.1 dev R2-eth0

R3 ip route add 175.7.7.0/24 via 174.1.1.1 dev R3-eth1
R3 ip route add 170.1.1.0/24 via 172.1.1.1 dev R3-eth0

R4 ip route add 175.7.7.0/24 via 175.7.7.2 dev R4-eth0
R4 ip route add 170.1.1.0/24 via 174.1.1.2 dev R4-eth2
R4 ip route add 170.1.1.0/24 via 173.1.1.2 dev R4-eth1
R4 ip route add 172.1.1.0/24 via 174.1.1.2 dev R4-eth2
R4 ip route add 171.1.1.0/24 via 173.1.1.2 dev R4-eth1

H2 ip route add 170.1.1.0/24 via 175.7.7.1 dev H2-eth0
H2 ip route add 173.1.1.0/24 via 175.7.7.1 dev H2-eth0
H2 ip route add 174.1.1.0/24 via 175.7.7.1 dev H2-eth0
H2 ip route add 171.1.1.0/24 via 175.7.7.1 dev H2-eth0
H2 ip route add 172.1.1.0/24 via 175.7.7.1 dev H2-eth0


R1 iptables -t nat -A POSTROUTING -o R1-eth0 -j MASQUERADE
R1 iptables -A FORWARD -i R1-eth0 -o R1-eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT
R1 iptables -A FORWARD -i R1-eth0 -o R1-eth1 -j ACCEPT

R2 iptables -t nat -A POSTROUTING -o R2-eth1 -j MASQUERADE
R2 iptables -A FORWARD -i R2-eth0 -o R2-eth1 -m state --state RELATED,ESTABLISHED -j ACCEPT
R2 iptables -A FORWARD -i R2-eth0 -o R2-eth1 -j ACCEPT

R4 iptables -t nat -A POSTROUTING -o R4-eth1 -j MASQUERADE
R4 iptables -A FORWARD -i R4-eth1 -o R4-eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
R4 iptables -A FORWARD -i R4-eth1 -o R4-eth0 -j ACCEPT


R4 iptables -A FORWARD -i R4-eth0 -o R4-eth2 -j ACCEPT
R4 iptables -A FORWARD -i R4-eth0 -o R4-eth2 -m state --state RELATED,ESTABLISHED -j ACCEPT
R4 iptables -t nat -A POSTROUTING -o R4-eth2 -j MASQUERADE

R3 iptables -A FORWARD -i R3-eth1 -o R3-eth0 -j ACCEPT
R3 iptables -A FORWARD -i R3-eth1 -o R3-eth0 -m state --state RELATED,ESTABLISHED -j ACCEPT
R3 iptables -t nat -A POSTROUTING -o R3-eth0 -j MASQUERADE