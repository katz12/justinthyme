from django.db import models

# Create your models here.
class Hardware(models.Model):
    class Meta:
        db_table = u'hardware'
    
    name = models.CharField(max_length=50)

class Recipe(models.Model):
    class Meta:
        db_table = u'recipe'
    id = models.AutoField(primary_key=True)
    url = models.URLField()
    img_url = models.URLField()
    name = models.CharField(max_length=200)
    course = models.CharField(max_length=20, null=True, blank=True)
    method = models.CharField(max_length=20, null=True, blank=True)
    difficulty = models.CharField(max_length=20, null=True, blank=True)
    wait_time = models.IntegerField()
    cook_time = models.IntegerField()
    hardware = models.ManyToManyField(Hardware)

