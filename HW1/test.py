import dns.name
import dns.message
import dns.query
import sys

def main():
	targetWeb = 'paypal.co'
	newNameserver = '198.41.0.4'
	target = '.'
	dtype = 'A'
	check_DNSSEC_Query = dns.message.make_query(target, dns.rdatatype.DNSKEY, want_dnssec = True)
	response1 = dns.query.tcp(check_DNSSEC_Query, newNameserver)

	query = dns.message.make_query(targetWeb, dtype, want_dnssec = True)
	response2 = dns.query.tcp(query, newNameserver)


	print len(response1.answer[0])
	if response1.rcode() != 0:
		print "server might have ERROR"
		exit()
	else:
		#case 2: DNSSEC is not enabled
		if len(response1.answer) == 0:
			print 'DNSSEC not supported'
			exit()
		# if response.answer[0].rdtype == dns.rdatatype.RRSIG:
		# 	print "in1"
		# 	rrsig, rrset = response.answer
		# elif response.answer[1].rdtype == dns.rdatatype.RRSIG:
		# 	print "in2"
		# 	rrset, rrsig = response.answer
		# else:
		# 	print 'DNSSEC not supported'
		#	exit()
		#key	
		key = {dns.name.from_text(target):response1.answer[0].to_rdataset()}
		#case 3: DNSSEC is configured but the digital signature could NOT be verified

		try:
			dns.dnssec.validate(response1.answer[0], response1.answer[1], key)
		except:
			print "DNSSec verification failed"
			exit()
		else:
			print "good"

if __name__ == '__main__':
	main()