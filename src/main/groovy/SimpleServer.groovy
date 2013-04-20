// File: SimpleServer.groovy

// The code block will be executed once.
if (init) {
    meta = [:]
    // We can access the socket in our script.
    meta.host = socket.inetAddress.hostAddress
}

// The line contains a single line of input from the client.
// If line starts with the text OUTPUT we output all gathered meta 
// information and close the client connection.
if (line.startsWith('OUTPUT')) {
    println()
    println "Server running on $meta.host"
    println "Meta info:"
    println "----------"
    meta.each { key, value -> 
        println "$key = $value"
    }
    println()
    return "success"  // Close client connection
} else {
    // Build meta information map.
    metaInfo = line.tokenize(":")  // Input meta information as key:value
    meta[metaInfo[0]] = metaInfo[1]
}