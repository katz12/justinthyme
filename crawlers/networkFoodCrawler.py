import time
import utils


def netCrawler(recipe_id, n):
#recipe_id = 0
#for n in range (0, 100):
    a= 12*n
    urln = 'http://www.foodnetwork.com/search/delegate.do?Ntk=site_search&Nr=Record%20Type:Result&N=501&No=' + str(a)
    time.sleep(1)#--#
    S = utils.getPage(urln)
    templst = []
    for u in S('.result-item.recipe'):
        templst.append(S(u))
    
    urlist = []
    for u in templst:
        urlist.append('http://www.foodnetwork.com' + u.find('a').attr('href'))
    
    print 'net list made ' + str(n)
    
    for nextUrl in urlist:
        time.sleep(1)#--#
        p = utils.getPage(nextUrl)
        
        RecpName = []
        for this in p('.fn_name'):
                RecpName.append(p(this).text())
        print RecpName

        if utils.isDuplicate(RecpName[0]):
            recipe_id += 1
            print 'duplicate'
            continue

        ImgURL = ''
        b = p('img#recipe-player-th')
        for this in b:
            ImgURL = p(this).attr('src') #if ImgURL is '' then there is no image given. Happens often.
        
        unparsedCtime = ''
        unparsedPtime = ''
        c = p('dd.clrfix')
        for this in c('meta'):
            if p(this).attr('itemprop') == 'cookTime':
                unparsedCtime = p(this).attr('content')
            elif p(this).attr('itemprop') == 'prepTime':
                unparsedPtime = p(this).attr('content')
        
        difficulty = ''
        difficulty = p(c[-1]).text()
    
        Ctime = utils.FN_minutesParser(unparsedCtime) + 60*(utils.FN_hourParser(unparsedCtime))
        Ptime = utils.FN_minutesParser(unparsedPtime) + 60*(utils.FN_hourParser(unparsedPtime))
        servings = []
        for this in c('span'):
            servings.append(p(this).text())
    
        IngrName = []
        d = p('.kv-ingred-list1')
        #d is now a list of all the ingred lists
        for this in d('li'):
            IngrName.append(p(this).text())
    
        #the ingredients here are smashed together with their ammounts, which I can't parse out.
        # if we can parse things like "1 large tomato, diced in 1/2-inch pieces" into ammounts and
        # ingredient lists, then someone should teach me.
    
        directions = []
        e = p('.fn_instructions')
        for this in e('p'):
            elf = p(this).text()
            if elf != '\n':
                if elf != '':
                    directions.append(elf)

        if not servings:
            servings = ['']
        
        decript = utils.descriptionize(directions)
        k = 0
        RecipeDict = {}
        RecipeDict = { 'id': recipe_id, 
            'name':RecpName[0], 
            'description' : decript,
            'servings' : servings[0], 
            'url' : nextUrl,
            'img_url' : ImgURL,
            'difficulty' : difficulty,
            'cook_time' : Ctime, 
            'prep_time' : Ptime}
        k = utils.insert('recipe', **RecipeDict)
        #print RecpName
        #for this in decript:
        #    print this
        #print servings[0]
        #print nextUrl
        #print ImgURL
        #print Ctime
        #print Ptime
   
        IngrDict = {}
        IngrAmnt = []
        if k != -1:
            for a in range(len(IngrName)):
                if len(IngrAmnt) < len(IngrName):
        	        IngrAmnt.append('') #in the case where "season to preference" is the 'ingredient'
        	        #print len(IngrAmnt)
                    #print IngrName[a]
                    #print IngrAmnt[a]
                IngrDict = { 'recipe_id':recipe_id, 
                    'ingredient_name':IngrName[a], 
                    'quantity':IngrAmnt[a]}
                l = utils.insert('recipe_ingredient', **IngrDict)
        recipe_id += 1
        
        
    return recipe_id


