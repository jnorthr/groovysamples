def addr(at) { return "http://www.ietf.org/rfc/rfc${at}.txt"  }
Integer.metaClass.getSeconds = { ->
    delegate * 1000
}

[6350,2045,2046].each
{ 
    def ad = addr(it)
    println ad 
    //localAddress: new InetSocketAddress(ad, 80)
    def url = ad.toURL() 
    //def content = url.getText()
    
    // Get content of URL with parameters.
    def content = url.getText(connectTimeout: 10.seconds, readTimeout: 10.seconds,
                          useCaches: true, allowUserInteraction: false,
                          requestProperties: ['User-Agent': 'Groovy Sample Script'])
    println "content="+content
    
    if (content!=null && content.size()>0)
    {
        println "there are ${content.size()} bytes available"
    }
}

url = "http://www.mrhaki.com/url.html".toURL()
content = url.getText(connectTimeout: 10.seconds, readTimeout: 10.seconds,
                          useCaches: true, allowUserInteraction: false,
                          requestProperties: ['User-Agent': 'Groovy Sample Script'])
println content    

url.newReader(connectTimeout: 10.seconds, useCaches: true).withReader { reader ->
    assert reader.readLine() == 'Simple test document'
}

println "------- the end -----------"