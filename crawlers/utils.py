import sys
sys.path.insert(0, '../')
from django.core.management import setup_environ
from justinthyme import settings
setup_environ(settings)

from recipe_app.models import Recipe
from django.db import connection, transaction
from pyquery import PyQuery as pq

# https://docs.djangoproject.com/en/dev/ref/exceptions/ 


tables = {   'recipe' : [ 
                        ('id', True),
                        ('name', True),
                        ('description', True),
                        ('servings', True),
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
        s += unicode(l) + ','
    s += unicode(list[-1]) + ')'
    return s

def makeDecimalTime(str):
    parsetime = ''     
    if str:
        parsetime = str[0]
    Time = 0
    for sp in parsetime.split():
        if sp.isdigit():
	        Time = int(sp)
    return Time

def FN_minutesParser(str):
    if str == '': 
        return 0
    if str[-3:-1].isdigit():
        return int(str[-3:-1])
    elif str[-2:-1].isdigit():
        return int(str[-2:-1])
    else: return 0


def FN_hourParser(str):
    if str == '':
        return 0
    if str[3] == 'H':
        if str[3].isdigit():
            return int(str[3])
        else: return 0
        return int(str[2])
    elif str[4] == 'H':
        if str[2:4].isdigit():
            return int(str[2:4])
        else: return 0
    else: return 0

def descriptionize(list):
    newlist = []
    newstring = ''
    for this in list:
        newlist.append(this + '\n' + '\n')
    for this in newlist:
        newstring += this
    return newstring

def isDuplicate(name):
    result = ''
    result = Recipe.objects.raw('SELECT name, id FROM recipe WHERE name LIKE %s', [name])
    awk = ''
    #avoids out of range errors
    for p in result:
        awk = p.name
    return awk == name

def newID(cur):
    result = ''
    while(True):
        result = Recipe.objects.raw('SELECT id FROM recipe WHERE id LIKE %s', [cur])
        awk = ''
        for p in result:
            awk = p.id
        if awk == cur:
            cur += 1
        else:
            return cur

def getPage(str):
    S = ''
    i = True
    while i:
        try:
            S = pq(url=str)
        except:
            print "trying connection..."
        else:
            i = False
        return S

#@transaction.commit_on_success
def insert(table_name, **kwargs):
    # Make sure we are using a valid table
    try:
        attrs = tables[table_name]
    except KeyError:
        print "utils.insert: invalid table name, valid tables are:" 
        for name in tables:
            print name
        return 0


    # Make sure none of the input parameters are invalid
    for key, value in kwargs.iteritems():
		if (key, True) in attrs or (key, False) in attrs:
			pass
		else:
			print "utils.insert: " + key + ' is not a valid attribute for ' + table_name
			return 0
    
    # Generate list of attrs and vals
    user_vals = []
    user_attrs = []
    for attr in attrs:
        val = kwargs.get(attr[0])
        if val == None:
            if attr[1] == True:
                print attr[0], "must have a value"
                return 0
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

    #duplicate checking
    dup = str(user_vals[1])
  #  result = ''
 #   result = Recipe.objects.raw('SELECT name, id FROM recipe WHERE name LIKE %s', [dup])
#    awk = ''
    #avoids out of range errors
 #   for p in result:
#        awk = p.name   
    #if duplicate found, return -1 error, no transaction committed awk != dup:
    if isDuplicate(dup):
        print 'duplicate not inserted'
        return -1
    else:
        cursor = connection.cursor()
        cursor.execute(sql, user_vals)
        transaction.commit_unless_managed()
        return 0
