import dns.name
import dns.message
import dns.query
import sys
import socket
import time
def main():
	start_time = time.time()
	args = sys.argv
	if(len(sys.argv) == 3):#google.com MX
		record = args[2]#A, MX, NS
		targetWeb = args[1]#google.com
	elif(len(sys.argv) == 2):#google.com
		record = "A"
		targetWeb = args[1]

	fp = open("root-servers.txt", "r")
	root_servers = fp.readlines()#!!!take care!!! each ip have a "\n" in the end
	fp.close()

	#A: 1 NS:2 MX:15
	dtype = ''
	if record == "A" or record == 'a':
		dtype = dns.rdatatype.A
	elif record == "NS" or record == "ns":
		dtype = dns.rdatatype.NS
	elif record == "MX" or record == 'mx':
		dtype = dns.rdatatype.MX
	else:
		print "other service not available now"
		return
	
	
	for ipAddr in root_servers:
		try:
			ans = dnsIterate(targetWeb, dtype, ipAddr.strip('\n'))
			if len(ans) == 0:
				continue
			if len(ans[0]) > 0:
				print ans[0]
				print("--- %s seconds ---" % (time.time() - start_time))
				return
		except:
			print "time out, next root server"

	print "can not find answer"

def dnsIterate(targetWeb, dtype, root_servers):
	#build message and send, receive
	query = dns.message.make_query(targetWeb, dtype)

	response = dns.query.udp(query, root_servers, timeout = 1)
	if len(response.answer) > 0:
		return response.answer
	else:
		if len(response.authority) <= 0:
			return response.answer
		else:#answer = 0, authority!=0   198.41.0.4
			#for ns in response.authority:    ??????
			newNS = socket.gethostbyname(str(response.authority[0][0]))
			return dnsIterate(targetWeb, dtype, str(newNS))

if __name__ == '__main__':
	main()

# rcode = response.rcode()
# if rcode != dns.rcode.NOERROR:
# 			if rcode == dns.rcode.NXDOMAIN:
# 				raise Exception('%s does not exist.' % targetWeb)
# 			else:
# 				raise Exception('Error %s' % dns.rcode.to_text(rcode))

