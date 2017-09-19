import dns.name
import dns.message
import dns.query
import sys

def main():
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
		ans = dnsIterate(targetWeb, 0, dtype, ipAddr.strip('\n'))
		if len(ans[0]) > 0:#ans[0] is RRset
			print ans[0]
			return

	print "can not find answer"

def dnsIterate(targetWeb, index, dtype, root_servers):#pir.org
	#build message and send, receive
	query = dns.message.make_query(targetWeb, dtype, want_dnssec = True)
	response = dns.query.tcp(query, root_servers)

	if len(response.answer) == 0:#NO DNS RECORD FOUND
		if len(response.additional) <= 0:
			return response.answer
		else:#answer = 0, authority!=0   198.41.0.4
			#for ns in response.authority:    ??????
			newNS = ''
			newNS = socket.gethostbyname(str(response.authority[0][0]))
			check(targetWeb, index, root_servers, response)
			index = index + 1
			return dnsIterate(targetWeb, index, dtype, str(newNS))

	else:
		return response.answer

def check(targetWeb, index, newNameserver, contentResponse):
	webList = targetWeb.split(".")
	target = ''
	for num in range(len(webList) - index, len(webList)):
		target = webList[num] + target + '.'
	if target == '':
		target = '.'

	#first: check if DNSSEC is enabled or not
	check_DNSSEC_Query = dns.message.make_query(target, dns.rdatatype.DNSKEY, want_dnssec = True)
	response = dns.query.tcp(check_DNSSEC_Query, newNameserver)

	if response.rcode() != 0:
		print "server might have ERROR"
		exit()
	else:
		#case 2: DNSSEC is not enabled
		if len(response.answer[0]) == 0:
			print 'DNSSEC not supported'
			exit()
		key = {dns.name.from_text(str(target)):response.answer[0]}
		#case 3: DNSSEC is configured but the digital signature could NOT be verified
		try:
			dns.dnssec.validate(response.answer[0], response.answer[1], key)
		except:
			print "DNSSec verification failed"
			exit()


if __name__ == '__main__':
	main()

