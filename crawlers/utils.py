import sys
sys.path.insert(0, '../')
from django.core.management import setup_environ
from justinthyme import settings
setup_environ(settings)


from django.db import connection, transaction

tables = { 'recipe' : [ 
                        ('name', True),
                        ('url', True),
                        ('img_url', True),
                        ('course', False),
                        ('method', False),
                        ('difficulty', False),
                        ('wait_time', False),
                        ('cook_time', True) 
                    ],
            
            'hardware' : [ 
                        ('name', True) 
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

# NOTE
# Do not use this for user generated values.
# This is prone to SQL Injection
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
        if key not in attrs:
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

            if isinstance(val, str):
                val = '"' + val + '"'
            user_vals.append(val)


    attr_string = makeListString(user_attrs)
    val_string = makeListString(user_vals)

    cursor = connection.cursor()
    sql = 'insert into %s %s values %s' % (table_name, attr_string, val_string)
    cursor.execute(sql)
    transaction.commit_unless_managed()

    
