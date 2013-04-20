$ cat dataset_test.groovy
import groovy.sql.Sql

//----------------------------------------
// definition of functions
//----------------------------------------
db = Sql.newInstance(
        'jdbc:sqlite:./ragtime.db',
        '',
        '',
        'org.sqlite.JDBC')

println db

musicianSet = db.dataSet('musician')

println musicianSet

musicianSet.each {
        println "${it.id} ${it.name}"
}

println ""

result = musicianSet.findAll{
        it.id >= 4
}
result.each {
        println it.name
}

println result.sql
println result.parameters

// To insert
musicianSet.add(
        id: 7,
        name: "Carlos Sanatana",
        gender: 'MALE'
)

println ""
result.each {
        println it.name
}

//$ groovy dataset_test.groovy