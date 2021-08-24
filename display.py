from introcs.turtle import *
from introcs import HSV
import argparse
import traceback
import os, os.path

class Vert:
    def __init__(self,i,p):
        assert type(p) == tuple
        self.index = i
        self.coord = p
        self.type  = ''
        self.next = None
        self.prev = None

    def __lt__(self, other):
        if (self.coord[1] == other.coord[1]):
            return self.coord[0] < other.coord[0]
        return self.coord[1] < other.coord[1]

    def __gt__(self, other):
        if (self.coord[1] == other.coord[1]):
            return self.coord[0] > other.coord[0]
        return self.coord[1] > other.coord[1]

    def __eq__(self, other):
        return (self.index == other.index)

    def __le__(self, other):
        return self == other or self < other

    def __ge__(self, other):
        return self == other or self > other

    def __ne__(self, other):
        return not (self == other)

    def ccw(self,a,b):
        val  = (b.coord[1] - a.coord[1]) * (self.coord[0] - a.coord[0])
        val -= (b.coord[0] - a.coord[0]) * (self.coord[1] - a.coord[1])
        return val > 0

    def __str__(self):
        value = str(self.index)+'.'+repr(self.coord)
        if self.type != '':
            value += '['+self.type+']'
        return value

    def __repr__(self):
        return str(self.__class__)+':'+str(self)

    def copy(self):
        result = Vert(self.index,self.coord)
        result.type = self.type
        result.next = self.next
        result.prev = self.prev
        return result


def parse():
    global SCALE
    parser = argparse.ArgumentParser(description='Triangulate a file.')
    parser.add_argument('file', type=str, help='an file of vertices (one per line)')
    parser.add_argument('-s','--scale', nargs='?', type=float, help='the turtle drawing scale')
    parser.add_argument('-r', '--rate',  nargs='?', type=int, help='the turtle drawing speed/rate')
    args = parser.parse_args()
    return args


def read_verts(file):
    try:
        result = []
        with open(file) as f:
            pos = 0
            head = None
            curr = None
            for line in f:
                if (line.strip() == '---'):
                    if not curr is None:
                        curr.next = head
                        head.prev = curr
                    head = None
                    curr = None
                else:
                    vert = Vert(pos,eval(line))
                    result.append(vert)
                    pos += 1
                    if head is None:
                        head = vert
                    if not curr is None:
                        curr.next = vert
                        vert.prev = curr
                    curr = vert
            if not curr is None:
                curr.next = head
                head.prev = curr
        return result
    except:
        traceback.print_exc()
        exit()


def read_categories(file):
    path = os.path.splitext(file)
    path = path[0]+'-categories'+path[1]
    try:
        result = []
        with open(path) as f:
            for line in f:
                pos = line.find('\t')
                row = eval(line[:pos])+(line[pos+1:].strip(),)
                result.append(row)
        return result
    except:
        print("Missing categories file for %s." % file)
        traceback.print_exc()
        exit()


def read_diagonals(file):
    path = os.path.splitext(file)
    path = path[0]+'-diagonals'+path[1]
    try:
        result = []
        with open(path) as f:
            for line in f:
                pos = line.find('\t')
                row = [eval(line[:pos]),eval(line[pos+1:])]
                result.append(row)
        return result
    except:
        print("Missing diagonals file for %s." % file)
        traceback.print_exc()
        exit()


def read_partitions(file):
    path = os.path.splitext(file)
    path = path[0]+'-partitions'+path[1]
    try:
        result = []
        with open(path) as f:
            poly = []
            for line in f:
                if (line.strip() == '---'):
                    result.append(poly)
                    poly = []
                else:
                    poly.append(eval(line))
        if poly:
            result.append(poly)
        return result
    except:
        print("Missing partition file for %s." % file)
        traceback.print_exc()
        exit()


def read_triangles(file):
    path = os.path.splitext(file)
    path = path[0]+'-triangles'+path[1]
    try:
        result = []
        with open(path) as f:
            poly = []
            for line in f:
                if (line.strip() == '---'):
                    result.append(poly)
                    poly = []
                else:
                    pos1 = line.find('\t')
                    pos2 = line.find('\t',pos1+1)
                    tri = (eval(line[:pos1]),eval(line[pos1+1:pos2]),eval(line[pos2+1:]))
                    poly.append(tri)
        if poly:
            result.append(poly)
        return result
    except:
        print("Missing triangles file for %s." % file)
        traceback.print_exc()
        exit()


def display(args,verts):
    w = Window()
    p = Pen(w)
    if not (args.rate is None):
        p.speed = args.rate
    else:
        p.speed = 9
    if not (args.scale is None):
        scale = args.scale
    else:
        scale = 1.0
        args.scale = scale

    visited = set()
    for v in verts:
        if not v.index in visited:
            visited.add(v.index)
            curr = v.prev
            p.move(curr.coord[0]*scale,curr.coord[1]*scale)
            p.drawTo(v.coord[0]*scale,v.coord[1]*scale)
            curr = v.next
            while curr != v:
                visited.add(curr.index)
                p.drawTo(curr.coord[0]*scale,curr.coord[1]*scale)
                curr = curr.next
    return p


def draw_categories(p,verts,scale=1.0):
    for v in verts:
        color = 'black'
        if v[2] == 'start':
            color = 'blue'
        elif v[2] == 'end':
            color = 'red'
        elif v[2] == 'split':
            color = 'green'
        elif v[2] == 'merge':
            color = 'white'
        p.move(v[0]*scale,v[1]*scale)
        p.solid = True
        p.fillcolor = color
        p.drawOval(10*scale,10*scale)
        p.solid = False


def draw_diagonals(p,diags,scale=1.0):
    for d in diags:
        p.move(d[0][0]*scale,d[0][1]*scale)
        p.drawTo(d[1][0]*scale,d[1][1]*scale)


def draw_partitions(p,parts,scale=1.0):
    p.fillcolor = 'light gray'
    for poly in parts:
        print(poly)
        end = poly[-1]
        p.move(end[0]*scale,end[1]*scale)
        p.solid = True
        for v in poly:
            p.drawTo(v[0]*scale,v[1]*scale)
        p.solid = False


def draw_triangles(p,tris,scale=1.0):
    for part in tris:
        for x in range(len(part)):
            color = HSV(360*x/len(part),1,1)
            p.fillcolor = color
            tri = part[x]
            p.move(tri[-1][0]*scale,tri[-1][1]*scale)
            p.solid = True
            for pos in tri:
                p.drawTo(pos[0]*scale,pos[1]*scale)
            p.solid = False
        pause()


def pause():
    input("Press [ANY] key  ")


if __name__ == '__main__':
    args = parse()
    verts = read_verts(args.file)
    p = display(args,verts)
    pause()

    cats = read_categories(args.file)
    draw_categories(p,cats,args.scale)
    pause()

    lines = read_diagonals(args.file)
    draw_diagonals(p,lines,args.scale)
    pause()

    parts = read_partitions(args.file)
    draw_partitions(p,parts,args.scale)
    pause()

    tris = read_triangles(args.file)
    draw_triangles(p,tris,args.scale)
