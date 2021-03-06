#from pyquery import PyQuery as pq
import time
import utils
#change the range to (1,2205) to get all A-Z pages
#20 results pr page
def allCrawler(recipe_id, n):
#recipe_id = 10
#for n in range(1, 220):
    a = n * 17
    #grab every 17th page, to provide a wide spread of results
    urln = 'http://allrecipes.com/recipes/ViewAll.aspx?Page=' + str(a)
    time.sleep(1)   #Sleeping is done according to the Web Crawling Standards S = pq(url=urln)
    S = utils.getPage(urln)
    templst = []
    for u in S('.rectitlediv'): 
        templst.append(S(u))
    urlist = []
    for u in templst: 
        urlist.append(u.find('a').attr('href'))
    print 'all list made ' + str(n)

    for nextUrl in urlist:
        time.sleep(1)		#Sleeping is done according to the Web Crawling Standards
        p = utils.getPage(nextUrl)
        #Creation of temp variables
        RecpName = []
        IngrName = []
        IngrAmnt = []
        Description = []
        ImageURL = []
        Cookpmin = []
        Cookphour = []
        Cookcmin = []
        Cookchour = []
        Cooktmin = []
        Cookthour = []
        Servings = []

        #temp variables filled
        for this in p('#itemTitle'): 
            RecpName.append(p(this).text())
        print RecpName

        if utils.isDuplicate(RecpName[0]):
            print 'duplicate'
            recipe_id += 1
            continue

        for this in p('#lblIngName'): 
	        if p(this).text() != '':
		        if p(this).attr('class') == 'ingredient-name':
			        IngrName.append(p(this).text())

        for this in p('#lblIngAmount'): 
            IngrAmnt.append(p(this).text())
        for this in p('span.plaincharacterwrap.break'): 
            Description.append(p(this).text())
        for this in p('#imgPhoto'): 
            ImageURL.append(p(this).attr('src'))

        for this in p('#prepMinsSpan'): 
            Cookpmin.append(p(this).text())
        for this in p('#cookMinsSpan'): 
            Cookcmin.append(p(this).text())
        for this in p('#totalMinsSpan'): 
            Cooktmin.append(p(this).text())

        for this in p('#prepHoursSpan'): 
            Cookphour.append(p(this).text())
        for this in p('#cookHoursSpan'): 
            Cookchour.append(p(this).text())
        for this in p('#totalHoursSpan'): 
            Cookthour.append(p(this).text())

        for this in p('#lblYield'): 
            Servings.append(p(this).text())


        #parse out the decimal times for cooking
        HpTime = utils.makeDecimalTime(Cookphour)
        MpTime = utils.makeDecimalTime(Cookpmin)

        #parse out the decimal times for preping
        HTime = utils.makeDecimalTime(Cookthour)
        MTime = utils.makeDecimalTime(Cooktmin)

        #convert all time into minutes
        Ctime = HTime*60 + MTime
        Ptime = HpTime*60 + MpTime

        #error checking
        if RecpName:
	        Rname = RecpName[0]
        else:
	        Rname = 'null'
        if ImageURL:
	        ImgURL = ImageURL[0]
        else:
	        ImgURL = 'null'
        if not Servings:
            Servings = ['null']

       
        decript = utils.descriptionize(Description)

        k = 0
        RecipeDict = { 'id': recipe_id, 
            'name':Rname, 
            'description' : decript,
            'servings' : Servings[0], 
            'url' : nextUrl,
            'img_url' : ImgURL, 
            'cook_time' : Ctime, 
            'prep_time' : Ptime}
        k = utils.insert('recipe', **RecipeDict)


        IngrDict = {}
        #if len(IngrName) > len(IngrAmnt):
	        #print IngrName
	        #print IngrAmnt
        if k != -1:
            for a in range(len(IngrName)):
                if len(IngrAmnt) < len(IngrName):
                    IngrAmnt.append('') #in the case where "season to preference" is the 'ingredient'
	                #print len(IngrAmnt)
                IngrDict = { 'recipe_id':recipe_id, 
                    'ingredient_name':IngrName[a], 
                    'quantity':IngrAmnt[a]}
    	        l = utils.insert('recipe_ingredient', **IngrDict)

        recipe_id += 1

    return recipe_id
