import sys
sys.path.insert(0, '../')
from django.core.management import setup_environ
from justinthyme import settings
setup_environ(settings)


from django.db import connection, transaction

# https://docs.djangoproject.com/en/dev/ref/exceptions/ 


tables = {  'hardware' : [ 
                        ('name', True) 
                    ],
            'ingredient' : [
                        ('name', True),
                        ('type', False),
                        ('dietary_value', False),
                        ('allergy', False),
                        ('vegan_vegetarian', False)
                    ],
            'recipe' : [ 
                        ('id', True),
                        ('name', True),
                        ('url', True),
                        ('img_url', True),
                        ('course', False),
                        ('method', False),
                        ('difficulty', False),
                        ('wait_time', False),
                        ('prep_time', False),
                        ('cook_time', True) 
                    ],
            'recipe_ingredient' : [
                        ('recipe_id', True),
                        ('ingredient_name', True),
                        ('quantity', True)
                    ],
            'recipe_hardware' : [
                        ('recipe_id', True),
                        ('hardware_name', True)
                    ],
            'user' : [
                        ('name', True),
                        ('pass_hash', True),
                        ('allergies', False)
                    ],
            'user_favorite' : [
                        ('user_name', True),
                        ('recipe_id', True)
                    ]
        }

def makeListString(list):
    # Format:
    # (x1,x2,x3,...,xn)

    s = '('
    for l in list[:-1]:
        s += str(l) + ','
    s += str(list[-1]) + ')'
    return s

@transaction.commit_on_success
def insert(table_name, **kwargs):
    # Make sure we are using a valid table
    try:
        attrs = tables[table_name]
    except KeyError:
        print "utils.insert: invalid table name, valid tables are:" 
        for name in tables:
            print name
        return


    # Make sure none of the input parameters are invalid
    for key, value in kwargs.iteritems():
		if (key, True) in attrs or (key, False) in attrs:
			pass
		else:
			print "utils.insert: " + key + ' is not a valid attribute for ' + table_name
			return
    
    # Generate list of attrs and vals
    user_vals = []
    user_attrs = []
    for attr in attrs:
        val = kwargs.get(attr[0])
        if val == None:
            if attr[1] == True:
                print attr[0], "must have a value"
                return
            else:
                next
        else:
            user_attrs.append(attr[0])
            user_vals.append(val)


    # Build SQL qeury
    # Format:
    # insert into <table_name> (attr1,attr2,...,attrn) values (val1,val2,...,valn)
    attr_string = makeListString(user_attrs)
    sql = 'insert into %s %s values (' % (table_name, attr_string)
    for _ in user_vals[:-1]:
        sql += '%s,'
    sql += '%s)'

    cursor = connection.cursor()
#    try:
    cursor.execute(sql, user_vals)
#    except DatabaseError:
#        print 'duplicate detected'
#        return

    transaction.commit_unless_managed()
#   #except DatabaseError:
#	#	print 'utils.insert: Duplicate entry'
    	

    
