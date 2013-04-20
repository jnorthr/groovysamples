import groovy.sql.Sql
// get JDBC driver fro: http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC#HowdoesSQLiteJDBCwork
// and append to classpath, then download sqlite from : http://www.sqlite.org/

//----------------------------------------
// definition of functions
//----------------------------------------
def selectAll(db) {
        db.eachRow( 'SELECT * FROM musician' ) { musician ->
                println musician.id  + ': ' + musician.name  + " -> " + musician.gender
        }
        println ""
}

db = Sql.newInstance(
        'jdbc:sqlite:./ragtime.db',
        '',
        '',
        'org.sqlite.JDBC')

println db

db.execute """
        DELETE FROM musician;
"""

db.execute """
        INSERT INTO musician (id, name, gender) VALUES ( 1, 'The Beatles', 'MALE' );
"""

def musicians = [
          [id: 2, name: 'Stevie Wonder', gender:'MALE']
        , [id: 3, name: 'Carol King', gender:'FEMALE']
        , [id: 4, name: 'Earth, Wind & Fire', gender:'GROUP']
        , [id: 5, name: 'Dunno who is', gender:'MALE']
]

//--------------------------------
// Using closure
//--------------------------------
musicians.each { musician ->
        db.execute """
                INSERT INTO musician (id, name, gender) VALUES (${musician.id}, ${musician.name}, ${musician.gender} );
        """
}

selectAll(db)

db.execute """
        UPDATE musician SET gender = 'GROUP' WHERE id = 1;
"""
selectAll(db)

//$ groovy crud.groovy