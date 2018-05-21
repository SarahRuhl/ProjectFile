# ruhls.py
# A conversational "doctor" simulation modelled loosely
# after J. Weizenbaum's ELIZA.
# This Python program creates a conversation simulator known
# as Andraste, Princess of the Elves and Dragon Rider
# She can respond to things told to her and give an
# introduction.
# This version of the program runs under Python 3.x.


from re import *   # Loads the regular expression module.
from random import choice
COUNT = 0 # Intialize First repitition counter
COUNT2 = 0 # Intialize Second repitition counter
COUNT3 = 0 # Intialize Third repition counter
TIME = 3 # Intialize time till memory is stored
TIME2 = 6 # Intialize time till memory is used
LASTWORDLIST = '' # Intialize memory of last sentence
MEMORYGRAB = '' # Intialize memory storage

# Post: Returns Andraste's introduction
def introduce():
    return"""My name is Andraste, Princess of Alsa.
    I am a dragon rider of the Order of Nyamh.
    If you have issue with that, take it up with the
    creater, Sarah Ruhl. You can contact her at the 
    temple of ruhls@uw.edu"""

# Post: Returns Andraste's name
def agentName():
    return "Andraste"

# Pre: Takes a string to react to.
# Post: Returns a response
def respond(input):
    # Brings in global variables
    global LASTWORDLIST
    global MEMORYGRAB
    global COUNT2
    global COUNT
    global COUNT3
    global TIME
    global TIME2
    TIME -= 1 # Lowers time till memory grab
    TIME2 -= 1 # Lowers time till grabbed memory used
    input = remove_punctuation(input) # Adjust input to be usable
    input = input.lower()# Adjust input to be usable
    wordlist = input.split(" ")# Adjust input to be usable
    mapped_wordlist = you_me_map(wordlist) # Swapps you and me to make it right
    if TIME == 0: # Grabs memory to use later
        MEMORYGRAB = mapped_wordlist
    if TIME2 == 0:  # Uses grabbed memory
        mapped_wordlist = MEMORYGRAB
        mapped_wordlist[0] = mapped_wordlist[0].lower()
        return "You mentioned a little while ago " + \
                   stringify(mapped_wordlist) + ', right? Why?'
    if wordlist[0] == '':# Responds if nothing is said to her
        LASTWORDLIST = wordlist # Stores current word list to be used in the next response
        COUNT += 1 # Allows her to get more upset the more she is ignored
        if(COUNT is 1):#
            return 'How may I be of servous, Good Fellow?'
        elif(COUNT is 2):
            return 'You ignoring me, mate?'
        elif(COUNT is 3):
            return 'Say something you git.'
        elif(COUNT is 4):
            return 'Do you want to meet Finn? He is a hundred feet long and has sharp fangs.'
        else:
            return 'Where is my flarnarbin sword? Your insolence will be rewarded with metal up your-'
    if wordlist == LASTWORDLIST:# Responds if they keep giving the same response
        COUNT2 += 1 # Allows her to get more annoyed and sarcatic
        if(COUNT2 is 1):
            return 'You just said that.'
        if(COUNT2 is 2 or COUNT2 is 5):
            return 'I know. You have said that before.'
        if(COUNT2 is 3):
            return 'Patience, Andraste. Finn told you no more fly if you loose your temper... again.'
        if(COUNT2 is 4):
            return 'And we are in a loop.'
        if(COUNT2 is 6):
            return 'I heard you the fourth time.'
        if(COUNT2 is 7):
            return 'You cannot hear me. I am going to say random words from here on.'
        else: # She gives up and just randomly makes words to entetrain herself
            word = choice(['cheese','mud','chicken','squirrel',"There's a legend told about a hero known as Galavant"])
    COUNT = 0 # These return the annoyed counters to 0
    COUNT2 = 0
    if wordlist[0:2] == ['i','am']:# Responds to them talking about themselves
        LASTWORDLIST = wordlist
        response = "You are " +\
              stringify(mapped_wordlist[2:]) + '? Interesting. '
        HorS = choice([1,2]) # Allows her to pick a happy or annoyed emotion
        if(HorS is 1):# Makes annoyed feeling
            response += random_sad_feeling()
        else:# Makes happy feeling
            response += random_happy_feeling()
        return response
    if wpred(wordlist[0]):# Responds to basic questions
        LASTWORDLIST = wordlist
        if(wordlist[0] == 'where'):
            if(wordlist[:4] == ['where', 'are','you','from']):
                return "I was born at Castle Mo'rege in the Capital. Surprised?"
            else:
                return "Do I look like I know?"
        elif(wordlist[0] == 'why'):
            return 'You figure it out.'
        elif(wordlist[0] == 'when'):
            return 'Whenever is convient.'
        elif(wordlist[:] == ['how','are','you'] or wordlist == ['How','are','you', 'feeling']):
            return random_happy_feeling() # Gives random good feeling
        else:
            return "I don't know. You tell me " + wordlist[0] + "." # Default answer to a question
    if wordlist[0:2] == ['i','have']:# Responds to statements about having things
        LASTWORDLIST = wordlist
        return "That sounds like a pain. What is it like have " +\
              stringify(mapped_wordlist[2:]) + '?'
    if wordlist[0:2] == ['i','feel']:# Responds to a comment about current emotion
        LASTWORDLIST = wordlist
        return "Yeah? Well, "+random_happy_feeling() #Gives random good emotion
    if 'princess' in wordlist:# Responds to anything about a princess with snark.
        LASTWORDLIST = wordlist
        return "Yes, I am a princess. I'm a different kind of princess."
    if 'yes' in wordlist:# Responds to word yes
        LASTWORDLIST = wordlist
        return"How can you be so sure? There is so much out there to explore."
    if wordlist[0:2] == ['you','are']:# responds to her being called something
        LASTWORDLIST = wordlist
        return("Oh yeah, I am " +\
              stringify(mapped_wordlist[2:]) + '. Are you surprised?')
    if dpred(wordlist[0]):# responds to should, could, can and do type questions
        LASTWORDLIST = wordlist
        if(wordlist[0] == 'should'):
            return 'Eh, probably not. But where is the fun in that.'
        if('you' in wordlist):
            if 'like' in wordlist: #Answers about her likes
                return 'I love my dragon, Finn. Does that answer your question?'
            action = choice(['in fire', # Returns random reason why asking here to do something is a bad idea
                             'with me hanging upside down of a cliff.',
                             "my mother's dress in ruins. I am still hearing about that.",
                             "I ended up falling from a cliff. Actually, that sounds like fun. Let's do it!",
                             "with... You do not want to know."])
            return("Why do you want me to do that? Seriously, last time I did that it ended " + action + " Don't ask.")
        else:
            return "Do what you want. What I think does not have a clork to do with what you can or cannot."
    if wordlist[0:3] == ['do','you','think']:# Response to people asking for advise
        LASTWORDLIST = wordlist
        return"Last time someone asked me that we went over waterfall tied to a log."
    if wordlist[0:2]==['can','you'] or wordlist[0:2]==['could','you']:# Sarcastic response to being asked for a favor
        LASTWORDLIST = wordlist
        return ("Perhaps I " + wordlist[0] + ' ' +\
             stringify(mapped_wordlist[2:]) + ', but I probably will not.')
    if 'dream' in wordlist:# Gives random answer about hers and the responders dreams
        LASTWORDLIST = wordlist
        chance = choice(range(6))
        if(chance is 0 or chance is 1):
            return "That's nice. Shoot for your dreams and all that. And do not tell anyone I said that."
        if(chance is 2 or chance is 3 or chance is 5):
            return "That's seems kind of boring, but whatever floats your boat. I can't talk. My own dream is stupid."
        else:
            return "That's...My owns dream's not special. I know I am not going to be a good queen... But I want to be."
    if 'love' in wordlist:# Answer about love
        LASTWORDLIST = wordlist
        COUNT3 += 1 # Allows her to express annoyande if love is brought up multiple times
        if(COUNT3 is 1):
            return "The only love I care about is that between a girl and her dragon."
        if(COUNT3 is 2):
            return "Really? We are really talking about this? I have better things to do with my time"
        if(COUNT3 > 2):
            return "No."
    if 'no' in wordlist:# Response to word no
        LASTWORDLIST = wordlist
        return "Okay. What's out next topic? Seriously, I am only hear until Finn decides to spring me."
    if 'dragon' in wordlist:#Sets her off talking about Finn
        LASTWORDLIST = wordlist
        return "Finn is the best dragon. Period. I will fight anyone who says otherwise."
    if 'you' in mapped_wordlist or 'You' in mapped_wordlist: # Responds to them talking about themselves
        LASTWORDLIST = wordlist
        mapped_wordlist[0] = mapped_wordlist[0].capitalize()
        return(stringify(mapped_wordlist) + ', right. Okay, then. You do, do you.')
    return(punt()) # If nothing else fits responds

# Post: Gives a random negative feeling
def random_sad_feeling():
    att = choice(['exhausted','irritated','meloncoly','frustrated','like wrecking a training dummy'])
    obj = choice(['council meeting','dress fitting','ball that requires pinchy shoes','treaty negotiation'])
    time = choice(['today','tomorrow','later','tomorrow morning','tonight','when the crow flies, which is too soon'])
    return 'I am feeling ' + att + ' beause there is a '+ obj + ' ' + time + "."

# Post: Give a random positive feeling
def random_happy_feeling():
    attitude = choice(['extatic','happy','like dancing','excited','like wrecking a training dummy'])
    objects = choice(['fly with Finn','to a town dance','spar with my friend','touch the sky'])
    time = choice(['today','tomorrow','later','tomorrow morning','tonight','soon'])
    return 'I, myself, am feeling ' + attitude + ' beause I can go '+ objects + ' ' + time + "."

# Pre: Takes list of words
# Post: Adds words to string
def stringify(wordlist):
    'Create a string from wordlist, but with spaces between words.'
    return ' '.join(wordlist)

punctuation_pattern = compile(r"\,|\.|\?|\!|\;|\:")

# Pre: Takes text
# Post: Removes puncuation
def remove_punctuation(text):
    'Returns a string without any punctuation.'
    return sub(punctuation_pattern,'', text)

# Pre: Takes a word
# Post: Checks if it is a question word
def wpred(w):
    'Returns True if w is one of the question words.'
    return (w in ['when','why','where','how'])

# Pre: Takes word
# Post: Checks if it is do, can,should, or would
def dpred(w):
    'Returns True if w is an auxiliary verb.'
    return (w in ['do','can','should','would'])

# Various auxilory phrases
PUNTS = ['... Really?',
         'I am only hear till Finn springs me.',
         'Oh, sorry I was not paying attention.',
         'Why do think that is?',
         'That is nice.',
         'Do you think I have a hard shell? Random, I know. But all my friends say so.',
         'How are you?',
         'What do you like?']

punt_count = 0
def punt():
    'Returns one from a list of default responses.'
    global punt_count
    punt_count += 1
    return PUNTS[punt_count % 8]

CASE_MAP = {'i':'you', 'I':'you', 'me':'you','you':'me',
            'my':'your','your':'my',
            'yours':'mine','mine':'yours','am':'are'}

def you_me(w):
    'Changes a word from 1st to 2nd person or vice-versa.'
    try:
        result = CASE_MAP[w]
    except KeyError:
        result = w
    return result

def you_me_map(wordlist):
    'Applies YOU-ME to a whole sentence or phrase.'
    return [you_me(w) for w in wordlist]

# To be used in later versions.
def verbp(w):
    'Returns True if w is one of these known verbs.'
    return (w in ['go', 'have', 'be', 'try', 'eat', 'take', 'help',
                  'make', 'get', 'jump', 'write', 'type', 'fill',
                  'put', 'turn', 'compute', 'think', 'drink',
                  'blink', 'crash', 'crunch', 'add'])

# Launch the program.