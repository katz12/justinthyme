import time
import utils

#recipe_id = utils.latest_recipe_id
abc = ['123','A','B','C','D','E','F','G','H','I','J','K','L',
'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']
a = ['A']
for n in a:
    
    urln = 'http://www.food.com/browse/allrecipes/index.zsp?letter=' + str(n)
    time.sleep(1)#--#
    S = utils.getPage(urln)
    templst = []
    for u in S('.list'):
        templst.append(S(u))
    urlist = []
    #only templst 0 and 1 have the results in themu
    for u in templst:
        urlist.append(u.find('a').attr('href'))
    print 'list made ' + str(n)

    for nextUrl in urlist:
        time.sleep(1)#--#
        p = utils.getPage(nextUrl)
        
        RecpName = []
        for this in p('#rz-lead'):
            if p(this).attr('class') == 'fn':
                RecpName.append(p(this).text())
        print RecpName

